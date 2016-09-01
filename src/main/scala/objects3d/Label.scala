package objects3d

import org.denigma.threejs._
import org.scalajs.dom
import org.scalajs.dom.html.Canvas
import org.scalajs.dom.raw.HTMLImageElement
import scala.scalajs.js

// https://stemkoski.github.io/Three.js/index.html
// https://github.com/stemkoski/stemkoski.github.com/tree/master/Three.js
object Label {

  def createLabel(text: String,position: Vector3): Object3D = {
    val canvas = dom.document.createElement( "canvas" ).asInstanceOf[Canvas]
    dom.console.log("Canvas width: "+canvas.width+" height: "+canvas.height)
    val xc = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    val fontSize = 14
    xc.font = "Bold "+fontSize+"px Arial"
    val metrics = xc.measureText( text )
    val borderThickness = 2
    val textWidth = metrics.width
    xc.fillStyle = "rgba(255,255,155,0.5)"
    xc.strokeStyle = "#000"
    xc.lineWidth = borderThickness
    xc.font = fontSize+"pt arial bold"
    roundRect(xc, borderThickness/2, borderThickness/2, textWidth * 1.4 + borderThickness , fontSize * 1.4 + borderThickness, 6)
    xc.fillStyle = "#000"
    xc.fillText(text, borderThickness+1, fontSize + borderThickness+1)
    val texture = new Texture(canvas)
    texture.needsUpdate = true
    val spriteMaterialParams: SpriteMaterialParameters = js.Dynamic.literal(
      map = texture
    ).asInstanceOf[SpriteMaterialParameters]
    val spriteMaterial = new SpriteMaterial(spriteMaterialParams)
    val label = new Sprite( spriteMaterial )
    label.scale.set(100,50,1.0)
    label.position.set(
      position.x,
      position.y,
      position.z)

    label
  }

  def roundRect(ctx: dom.CanvasRenderingContext2D, x:Double, y:Double, w:Double, h:Double, r:Int): Unit = {
    ctx.beginPath()
    ctx.moveTo(x+r, y)
    ctx.lineTo(x+w-r, y)
    ctx.quadraticCurveTo(x+w, y, x+w, y+r)
    ctx.lineTo(x+w, y+h-r)
    ctx.quadraticCurveTo(x+w, y+h, x+w-r, y+h)
    ctx.lineTo(x+r, y+h)
    ctx.quadraticCurveTo(x, y+h, x, y+h-r)
    ctx.lineTo(x, y+r)
    ctx.quadraticCurveTo(x, y, x+r, y)
    ctx.closePath()
    ctx.fill()
    ctx.stroke()
  }

  def apply(text: String,position: Vector3) : Object3D = createLabel(text,position)
}