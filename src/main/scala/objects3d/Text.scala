package objects3d

import org.denigma.threejs._

import scala.scalajs.js

object Text {
  // TODO: http://threejs.org/examples/webgl_geometry_text.html#00FFB2111#Goofball

  def apply(text: String, position: Vector3): Object3D = {
    val spriteMaterialParams: SpriteMaterialParameters = js.Dynamic.literal(
      map = LabelTexture(text,"Aqua") //"DeepOceanBlue"
    ).asInstanceOf[SpriteMaterialParameters]
    val spriteMaterial = new SpriteMaterial(spriteMaterialParams)
    val label = new Sprite( spriteMaterial )
    label.scale.set(100,30,1.0)
    label.position.set(
      position.x,
      position.y,
      position.z)
    label
  }
}
