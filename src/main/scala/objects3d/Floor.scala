package objects3d


import org.denigma.threejs.Object3D

import scala.scalajs.js

/**
  * Created by mariusz on 8/31/16.
  */
object Floor {
  private def pi = Math.PI

  def createMesh: Object3D = {
    import org.denigma.threejs._
    import THREE._
    // note: 4x4 checkboard pattern scaled so that each square is 25 by 25 pixels.
    val floorTexture = ImageUtils.loadTexture( "/target/scala-2.11/classes/3d/checkerboard.jpg" )
    floorTexture.wrapS = 1000.asInstanceOf[Wrapping] //RepeatWrapping
    floorTexture.wrapT = 1000.asInstanceOf[Wrapping] //RepeatWrapping
    floorTexture.repeat.set( 10, 10 )
    // DoubleSide: render texture on both sides of mesh
    def materialParams: MeshBasicMaterialParameters = js.Dynamic.literal(
      map = floorTexture,
      side = 2 //DoubleSide
    ).asInstanceOf[MeshBasicMaterialParameters]
    val floorMaterial = new MeshBasicMaterial(materialParams)
    val floorGeometry = new PlaneGeometry(1000, 1000, 1, 1)
    val floor = new Mesh(floorGeometry, floorMaterial)
    floor.position.y = pi / 2
    floor.position.x = 0
    floor.position.y = 0
    floor.position.z = 0
    floor
  }

  def apply(ignored: Unit) : Object3D = createMesh
}

