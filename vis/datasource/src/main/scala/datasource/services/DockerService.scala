package datasource.services

import com.spotify.docker.client.{DockerClient, DefaultDockerClient}

object  DockerService {
  import scala.concurrent.ExecutionContext.Implicits.global

  val  docker: DockerClient = new DefaultDockerClient("unix:///var/run/docker.sock")

  def test: Unit = {
//    import sys.process._
//
//    val json = "curl --unix-socket /var/run/docker.sock http:/containers/json" !!
//
//    println(s"CONTAINERS:$json")

    println(docker.info())
  }

}
