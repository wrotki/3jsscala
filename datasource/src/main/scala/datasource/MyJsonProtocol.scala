package datasource

import spray.httpx.SprayJsonSupport
import spray.json._
import DefaultJsonProtocol._

trait CompactJsonFormatSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val printer = CompactPrinter
  implicit val dockerImageFormat = jsonFormat2(DockerContainerData)
  implicit val dockerStateItemFormat = jsonFormat4(DockerState)
}

case class DockerContainerData(id: String, name: String)
case class DockerState(containers: Seq[DockerContainerData],servers: Seq[String], holdingTank: Seq[String], mortuary: Seq[String])
