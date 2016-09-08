package objects3d

import org.denigma.threejs._

import scala.scalajs.js


object SkyBox {

  def apply(ignored: Unit): Object3D = {
    val skyTexture = ImageUtils.loadTexture( Resources.loadPath("/3d/stars.jpg") )

    // DoubleSide: render texture on both sides of mesh
    def materialParams: MeshBasicMaterialParameters = js.Dynamic.literal(
      map = skyTexture,
      side = 2 //THREE.DoubleSide
    ).asInstanceOf[MeshBasicMaterialParameters]
    val textMaterial = new MeshBasicMaterial(materialParams).asInstanceOf[Material]
    val materialArray = js.Array(textMaterial,textMaterial,textMaterial,textMaterial,textMaterial,textMaterial)
    val meshFaceMat = new MeshFaceMaterial(materialArray)
    val cubeGeom = new BoxGeometry(50000,50000,50000,1,1,1)
    val boxMesh = new Mesh(cubeGeom,meshFaceMat)
    boxMesh
  }
}
