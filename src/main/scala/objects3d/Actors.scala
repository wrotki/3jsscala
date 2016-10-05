package objects3d

import org.denigma.threejs._

import scala.scalajs.js

/**
  * Created by mariusz on 8/26/16.
  */
object Actors {

  def materialParams: MeshLambertMaterialParameters = js.Dynamic.literal(
    color = new Color(0x564F8A) // wireframe = true
  ).asInstanceOf[MeshLambertMaterialParameters]

  def get: Seq[Object3D] = {
    val objects3d = 0 to 5 map { i: Int =>
        new Vector3(Math.sin(i.toDouble/10.0)*100,i*10,Math.cos(i.toDouble/10.0)*100-5000)
    } map { v: Vector3 =>
      val geometry = new BoxGeometry(10, 10, 10)
      val material = new MeshLambertMaterial(materialParams)
      val mesh: Mesh = new Mesh(geometry, material)
      mesh.name = v.toString
      mesh.position.set(v.x, v.y, v.z)
      mesh
    }
    Seq[Object3D]() :+ SkyBox() :+ Floor() :+ PolyverseLogo() :+ RegionBox("Active") :+ FirewallBox("WAF") :+
      Text(text="Warm up",position=new Vector3(-350,150,450)) :+
      Text(text="Serve",position=new Vector3(0,150,450)) :+
      Text(text="Purge/collect",position=new Vector3(350,150,450)) :+
      RequestParticles()
  }
}
