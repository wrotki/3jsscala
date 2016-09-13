package polyverse

import upickle.default._

case class DockerContainerData(id: String, name: String)
case class DockerState(containers: Seq[DockerContainerData],servers: Seq[String], holdingTank: Seq[String])


case class Cluster(containers: Seq[DockerContainerData],servers: Seq[String], holdingTank: Seq[String]) {
  var priorClusterState: Cluster = Cluster.emptyClusterState

  def newContainers: Set[String] = (containers.map { _.name } toSet) -- (priorClusterState.containers map { _.name })

  def removedContainers: Set[String] = (priorClusterState.containers.map { _.name } toSet) -- (containers map { _.name })

}

object Cluster{
  val emptyClusterState: Cluster = Cluster(Seq(),Seq(),Seq())
  def apply(json: String): Cluster = {
    val depickled = read[DockerState](json)
    new Cluster(containers=depickled.containers,servers=depickled.servers,holdingTank = depickled.holdingTank)
  }
}