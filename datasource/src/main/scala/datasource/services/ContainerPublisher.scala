package datasource.services

import akka.actor.Props
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream.actor.ActorPublisher
import com.spotify.docker.client.messages.Container
import spray.json._
import collection.JavaConverters._


object ContainerPublisher {
  def props: Props = Props[ContainerPublisher]
}
class ContainerPublisher extends ActorPublisher[TextMessage.Strict] with DockerJsonProtocol {

  def receive = {
    case containers: Seq[Container] =>

      val containerNames = containers map { c => c.names().asScala mkString }
      onNext(TextMessage(containerNames.toJson.toString))
    case x =>
      println(s"ContainerPublisher Actor received: $x")

  }
}
