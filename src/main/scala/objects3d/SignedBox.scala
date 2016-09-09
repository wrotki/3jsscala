package objects3d

import org.denigma.threejs._

import scala.scalajs.js


object SignedBox {

  def apply(text: String): Object3D = {
    val textTexture = LabelTexture(text)

    // DoubleSide: render texture on both sides of mesh
    def materialParams: MeshBasicMaterialParameters = js.Dynamic.literal(
      map = textTexture,
      side = 2 //THREE.DoubleSide
    ).asInstanceOf[MeshBasicMaterialParameters]
    val textMaterial = new MeshBasicMaterial(materialParams).asInstanceOf[Material]
    val materialArray = js.Array(textMaterial,textMaterial,textMaterial,textMaterial,textMaterial,textMaterial)
    val meshFaceMat = new MeshFaceMaterial(materialArray)
    val cubeGeom = new BoxGeometry(60,10,10,1,1,1)
    val boxMesh = new Mesh(cubeGeom,meshFaceMat)
    boxMesh
  }
}
