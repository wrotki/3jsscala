package objects3d

import org.denigma.threejs._
import scala.scalajs.js

object PolyverseLogo {

  def createMesh: Object3D = {
    import org.denigma.threejs._
    val logoTexture = ImageUtils.loadTexture( Resources.loadPath("/3d/polyverse-logo.png") )
    // DoubleSide: render texture on both sides of mesh
    def materialParams: MeshBasicMaterialParameters = js.Dynamic.literal(
      map = logoTexture,
      side = 2 //THREE.DoubleSide
    ).asInstanceOf[MeshBasicMaterialParameters]
    val logoMaterial = new MeshBasicMaterial(materialParams)
    val logoGeometry = new PlaneGeometry(300, 50, 1, 1)
    val logo = new Mesh(logoGeometry, logoMaterial)
    logo.position.set(0,200,500)
    logo
  }

  def apply(ignored: Unit) : Object3D = createMesh
}

