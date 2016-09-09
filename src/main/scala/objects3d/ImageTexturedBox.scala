package objects3d

import org.denigma.threejs._
import scala.scalajs.js

object ImageTexturedBox {
  def apply(text: String, texture: String, dimensions: Vector3, position: Vector3): Object3D = {
    val boxTexture = ImageUtils.loadTexture( Resources.loadPath(texture) )
    // DoubleSide: render texture on both sides of mesh
    def materialParams: MeshBasicMaterialParameters = js.Dynamic.literal(
      map = boxTexture,
      side = 2 //THREE.DoubleSide
    ).asInstanceOf[MeshBasicMaterialParameters]
    val textMaterial = new MeshBasicMaterial(materialParams).asInstanceOf[Material]
    val materialArray = js.Array(textMaterial,textMaterial,textMaterial,textMaterial,textMaterial,textMaterial)
    val meshFaceMat = new MeshFaceMaterial(materialArray)
    val cubeGeom = new BoxGeometry(dimensions.x,dimensions.y,dimensions.z,1,1,1)
    val boxMesh = new Mesh(cubeGeom,meshFaceMat)
    boxMesh.position.set(position.x,position.y,position.z)
    boxMesh
  }
}
