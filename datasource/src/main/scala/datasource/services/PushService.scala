package datasource.services

//  http://stackoverflow.com/questions/35246900/akka-http-websocket-akka-stream-use-websocket-as-a-sink

import java.util
import java.util.concurrent.TimeUnit

import akka.NotUsed
import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.ws.TextMessage.Strict
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Flow, Sink, Source}
import com.spotify.docker.client.messages.Container
import rx.{Observable, Scheduler}
import collection.JavaConverters._
import scala.concurrent.duration._

import spray.json._
import DefaultJsonProtocol._

import scala.collection.mutable
import scala.concurrent.forkjoin.ThreadLocalRandom

trait DockerJsonProtocol extends DefaultJsonProtocol {
  implicit val dockerImageFormat = jsonFormat1(DockerContainerData)
}

case class DockerContainerData(
                                id: String
                              )

object PushService extends WebService with DockerJsonProtocol {


  override def route: Route =  path("status") {

//    import sys.process._
//
//    val json = "curl --unix-socket /var/run/docker.sock http:/containers/json" !!
//
//    val src = Source(Seq(TextMessage(json)) toList)

    val containers: List[Container] = (DockerService.docker.listContainers() asScala) toList
    val containersForClient: List[Strict] = containers map { c => TextMessage(DockerContainerData(id=c.id()).toJson.toString) }
    val src: Source[Strict, NotUsed] = Source(containersForClient)

    val containersSource = Source.actorPublisher[Strict](ContainerPublisher.props)

    val o = Observable.interval(5L, TimeUnit.SECONDS)
    o.subscribe(n => println("n = " + n))

    extractUpgradeToWebSocket { upgrade =>
      complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, containersSource))
    }
  }
}