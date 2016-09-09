package objects3d

import org.denigma.threejs._

object RegionBox {
  def apply(text: String): Object3D = {
    ImageTexturedBox(text,"/3d/water.jpg",new Vector3(333,10,1000),new Vector3(0,10,0))
  }
}

object FirewallBox {
  def apply(text: String): Object3D = {
    ImageTexturedBox(text,"/3d/lava.jpg",new Vector3(1000,333,10),new Vector3(0,0,500))
  }
}
