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
      size = 20,
      map = ImageUtils.loadTexture(
        Resources.loadPath("/3d/particle.png")
      ),
      transparent = true,
      blending = 2 //THREE.AdditiveBlending
    ).asInstanceOf[PointCloudMaterialParameters]
    val pMaterial = new PointCloudMaterial(pointCloudParams)

    (0 to particleCount) map { _ => new Vector3(
      Math.random() * 500 - 250,
      Math.random() * 500 - 250,
      Math.random() * 500 + 1500
    )
    } foreach {
      particles.vertices.push(_)
    }

    // create the particle system
    val cloud = new PointCloud(
      particles,
      pMaterial)

    val pindexed = particles.vertices.zipWithIndex
    val tweenProps = js.Dynamic.literal(
      loop = true
    )
    pindexed foreach { v =>
      val tweenLength = 7500
      val pl = js.Dynamic.literal(x = Math.random() * 200 - 100, y = 50, z = 0)
      Tween.get(particles.vertices(v._2), tweenProps).wait(v._2 * 15, false).to(pl, tweenLength, Ease.getPowInOut(4))
    }

    setInterval(100) {
      pindexed foreach { v =>
        particles.verticesNeedUpdate = true
      }
    }
    cloud
  }
}
