package objects3d

import org.denigma.threejs.Texture
import org.scalajs.dom
import org.scalajs.dom.html._


object TranslucentTexture {

  def apply(text: String): Texture = {
    val (w,h) = (512,512)
    val canvas = dom.document.createElement( "canvas" ).asInstanceOf[Canvas]
    canvas.width = w
    canvas.height = h
    ///dom.console.log("Canvas width: "+canvas.width+" height: "+canvas.height)
    val xc = canvas.getContext("2d").asInstanceOf[dom.CanvasRenderingContext2D]
    xc.fillStyle = "rgba(255,10,10,0.1)"
    xc.beginPath()
    xc.rect(0,0,w,h)
    xc.fill()
    // Label
    val fontSize = 24
    xc.font = "Bold "+fontSize+"px Arial"
    val metrics = xc.measureText( text )
    val borderThickness = 2
    val textWidth = metrics.width
//    xc.strokeStyle = "#000"
//    xc.lineWidth = borderThickness
//    xc.font = fontSize+"pt arial bold"
//    xc.fillStyle = "#000"
//    xc.fillText(text, borderThickness+1, 48 + fontSize + borderThickness+1)
    val texture = new Texture(canvas)
    texture.needsUpdate = true
    texture
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
}
