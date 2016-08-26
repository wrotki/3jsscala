package org.denigma.threejs.controls

import org.denigma.threejs.Vector2
import org.denigma.threejs.extensions.controls.CameraControls
import org.scalajs.dom
import org.scalajs.dom.raw.MouseEvent

import scala.scalajs.js

trait MoveControls extends CameraControls {

  var minDistance = 0
  var maxDistance = Double.MaxValue //infinity?

  protected def computeMove(event: MouseEvent): Unit ={
  }
}
