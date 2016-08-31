package datasource.services

import akka.actor.Props
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream.actor.ActorPublisher
import com.spotify.docker.client.messages.Container
import spray.json._


object ContainerPublisher {
  def props: Props = Props[ContainerPublisher]
}
class ContainerPublisher extends ActorPublisher[TextMessage.Strict] with DockerJsonProtocol {

  def receive = {
    case container: Container =>
      onNext(TextMessage(DockerContainerData(id=container.id).toJson.toString))
    case x =>
      println(s"ContainerPublisher Actor received: $x")

  }
}
