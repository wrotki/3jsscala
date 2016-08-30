package datasource

import spray.httpx.SprayJsonSupport
import spray.json._
import DefaultJsonProtocol._

trait CompactJsonFormatSupport extends DefaultJsonProtocol with SprayJsonSupport {
  implicit val printer = CompactPrinter
  implicit val compactPrintedItemFormat = jsonFormat3(Person)
//  implicit val diPrintedItemFormat = jsonFormat6(DockerImage)
//  implicit val rtPrintedItemFormat = jsonFormat2(RepositoryTag)
}

case class Person(name: String, firstName: String, age: Long)