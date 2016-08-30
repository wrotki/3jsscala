package datasource

//  http://blog.scalac.io/2015/07/30/websockets-server-with-akka-http.html
// http://doc.akka.io/docs/akka/2.4.9/scala/http/routing-dsl/websocket-support.html#server-side-websocket-support-scala

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._

import akka.stream.ActorMaterializer
import datasource.services.{DockerService, PushService, EchoService, MainService}

import scala.io.StdIn
import scala.util.{Failure, Success, Try}

object Server extends App {
  DockerService.test


  implicit val actorSystem = ActorSystem("akka-system")
  implicit val flowMaterializer = ActorMaterializer()

  val interface = "localhost"
  val port = 8080

  val route = MainService.route ~
    EchoService.route ~
    PushService.route

  val binding = Http().bindAndHandle(route, interface, port)
  println(s"Server is now online at http://$interface:$port\nPress RETURN to stop...")
  if ( StdIn.readLine() == null){
    println("Went wrong, null from readline")
  }

  import actorSystem.dispatcher

  binding.flatMap(_.unbind()).onComplete(_ => actorSystem.terminate())
  println("Server is down...")

}
