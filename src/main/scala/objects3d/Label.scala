package objects3d

import org.denigma.threejs._
import org.scalajs.dom
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.raw.HTMLImageElement
import scala.scalajs.js

// https://stemkoski.github.io/Three.js/index.html
// https://github.com/stemkoski/stemkoski.github.com/tree/master/Three.js
object Label {

  def createLabel(text: String,position: Vector3): Object3D = {
    val spriteMaterialParams: SpriteMaterialParameters = js.Dynamic.literal(
      map = LabelTexture(text)
    ).asInstanceOf[SpriteMaterialParameters]
    val spriteMaterial = new SpriteMaterial(spriteMaterialParams)
    val label = new Sprite( spriteMaterial )
    label.scale.set(100,50,1.0)
    label.position.set(
      position.x,
      position.y,
      position.z)
    label
  }


  def apply(text: String,position: Vector3) : Object3D = createLabel(text,position)
}