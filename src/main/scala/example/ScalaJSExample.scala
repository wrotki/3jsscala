package example

import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom.html

@JSExport
object ScalaJSExample {

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val el: HTMLElement = dom.document.getElementById("container").asInstanceOf[HTMLElement]
    val demo = new ExampleScene(el, 2000, 2000) // scalastyle:ignore
    demo.render()
  }
}
