package datasource.services

//  http://stackoverflow.com/questions/35246900/akka-http-websocket-akka-stream-use-websocket-as-a-sink

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Sink, Source, Flow}

import scala.concurrent.forkjoin.ThreadLocalRandom

object PushService extends WebService {

  override def route: Route =  path("status") {
    val src =
      Source.fromIterator(() => Iterator.continually(ThreadLocalRandom.current.nextInt()))
        .filter(i => i > 0 && i % 2 == 0).map(i => TextMessage(i.toString))
    import akka.http.scaladsl.server.Directives._

    extractUpgradeToWebSocket { upgrade =>
      complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, src))
    }
  }

  val echoService: Flow[Message, Message, _] = Flow[Message].map {
    case TextMessage.Strict(txt) => TextMessage("ECHO: " + txt)
    case _ => TextMessage("Message type unsupported")
  }
}
