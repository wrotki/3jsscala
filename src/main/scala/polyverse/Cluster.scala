package polyverse

import org.scalajs.dom
import upickle.default._

case class DockerContainerData(id: String, name: String)

case class DockerState(containers: Seq[DockerContainerData], servers: Seq[String], holdingTank: Seq[String], mortuary: Seq[String])


case class Cluster(containers: Seq[DockerContainerData], servers: Seq[String], holdingTank: Seq[String], mortuary: Seq[String]) {
  var priorClusterState: Cluster = Cluster.emptyClusterState

  def newContainers: Set[String] = (containers.map {
    _.name
  } toSet) -- (priorClusterState.containers map {
    _.name
  })

  def removedContainers: Set[String] = (priorClusterState.containers.map {
    _.name
  } toSet) -- (containers map {
    _.name
  })

  def systemContainers: Set[String] = {
    val res = containers filter {
      _.name startsWith "polyverse"
    } map {
      _.name
    } toSet

    dom.console.log("System containers:" + res)
    res
  }

  def warmingUpContainers: Set[String] = {
    val res = containers filter {
      holdingTank contains _.id
    } map {
      _.name
    } toSet

    dom.console.log("Warming up containers:" + res)
    res
  }

  def servingContainers: Set[String] = {
    val res = containers filter {
      servers contains _.name
    } map {
      _.name
    } toSet

    dom.console.log("Serving containers:" + res)
    res
  }

  def garbageContainers: Set[String] = {
    val res = containers filter {
      mortuary contains _.id
    } map {
      _.name
    } toSet

    dom.console.log("Garbage containers:" + res)
    res
  }

}

object Cluster {
  val emptyClusterState: Cluster = Cluster(Seq(), Seq(), Seq(),Seq())

  def apply(json: String): Cluster = {
    val depickled = read[DockerState](json)
    new Cluster(containers = depickled.containers, servers = depickled.servers, holdingTank = depickled.holdingTank, mortuary = depickled.mortuary)
  }
}