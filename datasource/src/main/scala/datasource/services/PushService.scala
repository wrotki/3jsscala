package datasource.services

//  http://stackoverflow.com/questions/35246900/akka-http-websocket-akka-stream-use-websocket-as-a-sink

import akka.actor.{ActorSystem}
import akka.http.scaladsl.model.ws.TextMessage.Strict
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{Sink, Source}
import datasource.{CompactJsonFormatSupport, DockerContainerData, DockerState}
import rx.lang.scala.Observable
import collection.JavaConverters._
import scala.concurrent.duration._
import scala.util.Try
import sys.process._
import upickle.default._


case class ServerLine(URL: String, ContainerName: String, CreatedTime: String, PlacedInServiceTime: String, LeaseId: Double)

object PushService extends WebService with CompactJsonFormatSupport {

  implicit val system = ActorSystem()

  override def route: Route = path("status") {

    val containersSource = Source.actorPublisher[Strict](ContainerPublisher.props)
    val dataPublisherRef = system.actorOf(ContainerPublisher.props)


    val o = Observable.interval(2 seconds)
      .onErrorReturn(e => 0L)
      .subscribe((n) => {
        val svrs = for {
          scoll <- servers
          sseq <- Try(scoll.split("\n").toSeq)
          depickled <- Try(
            sseq map { s =>
              println("Server: " + s)
              val smap:ServerLine = read[ServerLine](s)
              smap.ContainerName
            }
          )
        } yield depickled
        println("Servers:" + svrs)

        val hTank = for {
          htcoll <- holdingTank
          htseq <- Try(htcoll.split("\n").toSeq)
          names <- Try(
            htseq map { ht =>
              println("Htank item: " + ht)
              val segs = ht.split("/").toSeq
              println("Htank split: " + segs)
              segs(4)
            } filter { s => !s.isEmpty }
          )
        } yield names
        println("Holding tank:" + hTank)

        val mTary = for {
          htcoll <- mortuary
          htseq <- Try(htcoll.split("\n").toSeq)
          names <- Try(
            htseq map { ht =>
              println("Mtary item: " + ht)
              val segs = ht.split("/").toSeq
              println("Mtary split: " + segs)
              segs(4)
            } filter { s => !s.isEmpty }
          )
        } yield names
        println("Mortuary:" + mTary)

        val containers: Seq[DockerContainerData] = DockerService.docker.listContainers().asScala map { c => DockerContainerData(id = c.id(), name = (c.names().asScala mkString).substring(1)) }

        dataPublisherRef ! DockerState(containers = containers,
          servers = svrs.getOrElse(Seq()),
          holdingTank = hTank.getOrElse((Seq())),
          mortuary    = mTary.getOrElse((Seq())))
        //println("sent")
      },
        e => println(e))

    extractUpgradeToWebSocket { upgrade =>
      complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, Source.fromPublisher(ActorPublisher(dataPublisherRef))))
    }

  }

  def servers: Try[String] = Try("/usr/local/bin/docker exec polyverse_etcd_1 etcdctl get --prefix --keys-only / " #|
    "grep servers/" #|
    "xargs -L 1 /usr/local/bin/docker exec polyverse_etcd_1 etcdctl get" #|
    "grep ContainerName"
    !!)

  def holdingTank: Try[String] = Try("/usr/local/bin/docker exec polyverse_etcd_1 etcdctl get --prefix --keys-only / " #|
    "grep edenspace" !!)

  def mortuary: Try[String] = Try("/usr/local/bin/docker exec polyverse_etcd_1 etcdctl get --prefix --keys-only / " #|
    "grep mortuary" !!)

}
