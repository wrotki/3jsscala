package objects3d

import org.denigma.threejs._
import scala.scalajs.js

object Floor {

  def createMesh: Object3D = {
    import org.denigma.threejs._
    // note: 4x4 checkboard pattern scaled so that each square is 25 by 25 pixels.
    val floorTexture = ImageUtils.loadTexture( Resources.loadPath("/3d/bluebackgrnd.png") )
    floorTexture.wrapS = 1000.asInstanceOf[Wrapping] //THREE.RepeatWrapping
    floorTexture.wrapT = 1000.asInstanceOf[Wrapping] //THREE.RepeatWrapping
    floorTexture.repeat.set( 10, 10 )
    // DoubleSide: render texture on both sides of mesh
    def materialParams: MeshBasicMaterialParameters = js.Dynamic.literal(
      map = floorTexture,
      side = 2 //THREE.DoubleSide
    ).asInstanceOf[MeshBasicMaterialParameters]
    val floorMaterial = new MeshBasicMaterial(materialParams)
    val floorGeometry = new PlaneGeometry(1000, 1000, 1, 1)
    val floor = new Mesh(floorGeometry, floorMaterial)
    floor.position.set(0,0,0)
    floor.rotation.set(Math.PI/2,0,0)
    floor
  }

  def apply(ignored: Unit) : Object3D = createMesh
}

