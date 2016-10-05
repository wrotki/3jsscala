package objects3d

import com.scalawarrior.scalajs.createjs.{Ease, Tween}
import org.denigma.threejs._
import scala.scalajs.js.timers._

import org.scalajs.dom

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
      Math.random() * 500 + 2500
    )
    } foreach {
      particles.vertices.push(_)
    }

    // create the particle system
    val cloud = new PointCloud(
      particles,
      pMaterial)


    val dist = new Vector3(50000, 50000, 50000)
    val pindexed = particles.vertices.zipWithIndex
    val pl = js.Dynamic.literal(x = 0, y = 0, z = 0)
    val tweenProps = js.Dynamic.literal(
      loop = true
    )
    pindexed foreach { v =>
      val tweenLength = 7500
      Tween.get(particles.vertices(v._2),tweenProps).wait(v._2 * 15, false).to(pl, tweenLength, Ease.getPowInOut(4))
    }

    setInterval(100) {
      pindexed foreach { v =>
        particles.verticesNeedUpdate = true
      }
    }
    cloud
  }
}
