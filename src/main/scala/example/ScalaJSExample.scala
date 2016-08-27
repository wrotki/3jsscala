package example

import org.scalajs.dom.raw.HTMLElement

import scala.scalajs.js.annotation.JSExport
import org.scalajs.dom
import org.scalajs.dom._

@JSExport
object ScalaJSExample {

  @JSExport
  def main(canvas: html.Canvas): Unit = {
    val el: HTMLElement = dom.document.getElementById("container").asInstanceOf[HTMLElement]
    val MARGIN = 0
    val WIDTH = window.innerWidth
    val HEIGHT = window.innerHeight - 2 * MARGIN
    val demo = new ExampleScene(el, WIDTH, HEIGHT) // scalastyle:ignore
    demo.render()
  }
}
