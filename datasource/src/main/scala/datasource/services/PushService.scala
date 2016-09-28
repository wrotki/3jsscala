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
              val smap = read[Map[String, String]](s)
              smap("ContainerName")
            }
          )
        } yield depickled
        println("Servers:" + svrs)

        val hTank = for {
          htcoll <- holdingTank
          htseq <- Try(htcoll.split("\n").toSeq)
          names <- Try(
            htseq map { ht =>
              val segs = ht.split("/")
              if (segs.length > 3) segs(3) else ""
            } filter { s => !s.isEmpty }
          )
        } yield names
        println("Holding tank:" + hTank)

        val containers: Seq[DockerContainerData] = DockerService.docker.listContainers().asScala map { c => DockerContainerData(id = c.id(), name = (c.names().asScala mkString).substring(1)) }

        dataPublisherRef ! DockerState(containers = containers,
          servers = svrs.getOrElse(Seq()),
          holdingTank = hTank.getOrElse((Seq())))
        //println("sent")
      },
        e => println(e))

    extractUpgradeToWebSocket { upgrade =>
      complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, Source.fromPublisher(ActorPublisher(dataPublisherRef))))
    }

  }

  def servers: Try[String] = Try("docker exec polyverse_etcd_1 /etcdctl ls --recursive / " #|
    "grep servers/" #|
    "xargs -L 1 docker exec polyverse_etcd_1 /etcdctl get" !!)

  def holdingTank: Try[String] = Try("docker exec polyverse_etcd_1 /etcdctl ls --recursive / " #|
    "grep holdingtank/" !!)

}
