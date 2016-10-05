package objects3d

import org.denigma.threejs._

import scala.scalajs.js

// https://aerotwist.com/tutorials/creating-particles-with-three-js/

object RequestParticles {

  def apply(ignored: Unit): Object3D = {
    val particleCount = 1800
    val particles = new Geometry()
    def pointCloudParams: PointCloudMaterialParameters = js.Dynamic.literal(
      color = 0xFFFFFF,
      size = 20
    ).asInstanceOf[PointCloudMaterialParameters]
    val pMaterial = new PointCloudMaterial(pointCloudParams)

    (0 to particleCount) map { _ => new Vector3(
      Math.random() * 500 - 250,
      Math.random() * 500 - 250,
      Math.random() * 500 + 1250
    )
    } foreach {
      particles.vertices.push(_)
    }

    // create the particle system
    new PointCloud(
      particles,
      pMaterial)
  }
}
