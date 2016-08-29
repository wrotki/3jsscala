package datasource.services

//  http://stackoverflow.com/questions/35246900/akka-http-websocket-akka-stream-use-websocket-as-a-sink

import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Sink, Source, Flow}
import datasource.{CompactJsonFormatSupport, Person}
import spray.json._

import scala.concurrent.forkjoin.ThreadLocalRandom

object PushService extends WebService with CompactJsonFormatSupport {

  override def route: Route =  path("status") {
    val p = Person("John","Smith",20)
    val src =
      Source.fromIterator(() => Iterator.continually(ThreadLocalRandom.current.nextInt()))
        .filter(i => i > 0 && i % 2 == 0).map(i => TextMessage(i.toString))

    val src2 = Source.fromIterator(() => Iterator.continually(p)).map(p => TextMessage(p.toJson))
    import akka.http.scaladsl.server.Directives._

    extractUpgradeToWebSocket { upgrade =>
      complete(upgrade.handleMessagesWithSinkSource(Sink.ignore, src2))
    }
  }

  val echoService: Flow[Message, Message, _] = Flow[Message].map {
    case TextMessage.Strict(txt) => TextMessage("ECHO: " + txt)
    case _ => TextMessage("Message type unsupported")
  }
}
