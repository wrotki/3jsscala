package objects3d

import org.denigma.threejs.Vector3

object LayoutCurve {

  def grid(linearPosition: Double): Vector3 = {
    val gridSize = 300.0
    val gap = 100.0
    val x = -gridSize/2.0 + gap * linearPosition % gridSize
    val y = 50.0
    val z = 150.0 - gap * Math.floor(linearPosition * gap / gridSize)
    new Vector3(x,y,z)
  }

}
