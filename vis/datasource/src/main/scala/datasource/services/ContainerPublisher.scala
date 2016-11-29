package datasource.services

import akka.actor.Props
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream.actor.ActorPublisher
import com.spotify.docker.client.messages.Container
import datasource.{CompactJsonFormatSupport, DockerState}
import spray.json._
import collection.JavaConverters._


object ContainerPublisher {
  def props: Props = Props[ContainerPublisher]
}
class ContainerPublisher extends ActorPublisher[TextMessage.Strict] with CompactJsonFormatSupport {

  def receive = {
    case state: DockerState =>
      val stateJson = state.toJson.toString
      println(stateJson)
      onNext(TextMessage(stateJson))
    case x =>
      println(s"ContainerPublisher Actor received: $x")

  }
}
