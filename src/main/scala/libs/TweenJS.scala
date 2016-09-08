package libs

import org.denigma.threejs.Vector3

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName

@js.native
trait Tween extends js.Object {

  def to(props: Vector3,duration: Int): Tween = js.native

}

@JSName("createjs.Tween")
@js.native
object Tween extends js.Object {

  def get(target: js.Object): Tween = js.native

}

