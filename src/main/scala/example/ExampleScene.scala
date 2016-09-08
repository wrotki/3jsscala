package example

import libs.Tween
import objects3d.{LayoutCurve, SignedBox, Label, Actors}
import org.denigma.threejs.extensions.controls.{JumpCameraControls, CameraControls}
import org.denigma.threejs._
import org.denigma.threejs.extensions.Container3D
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.raw.HTMLElement
import upickle.default._

// scalastyle:off
class ExampleScene(val container: HTMLElement, val width: Double, val height: Double) extends Container3D {


  camera.position.y = 150
  camera.position.z = 500
//  override val controls: CameraControls = new ExampleControls(
//    camera, this.container, scene, width, height, new Vector3(0,0,0))

  setupRenderer

  val light = new DirectionalLight(0x88ffff, 2)
  light.position.set(1, 1, 1).normalize()
  scene.add(light)


  val light2 = new DirectionalLight(0xff88ff, 2)
  light.position.set(-1, -1, -1).normalize()
  scene.add(light2)


  val constantMeshes = Actors.get
  var meshes = constantMeshes
  meshes foreach(scene.add)

  val datasource = new WebSocket(getWebsocketUri(org.scalajs.dom.document, "status"))

  var prevContainers = Seq[Object3D]()

  datasource.onmessage = { (event: MessageEvent) ⇒
    //dom.console.log(event.data.toString)
    val depickled = read[Seq[String]](event.data.toString)
    val labelsZipped = depickled.zipWithIndex
    val containerLabels = labelsZipped map { t =>
      val box = SignedBox(t._1)//Label(t._1,labelLocation(t._2))
      val p =labelLocation(t._2)
      //dom.console.log("Container location: "+p.x+","+p.y+","+p.z)
      box.position.set(p.x,p.y,p.z)
//      val newTween = Tween.get(box.position)
//      newTween.to(p,1000)
      box
    }


    prevContainers foreach scene.remove
    /* Actors.get ++ */containerLabels foreach scene.add
    prevContainers = containerLabels
  }

  def labelLocation(i: Int):Vector3 = {
    LayoutCurve.grid(i)
  }


  private def getWebsocketUri(document: Document, socketId: String): String = {
    val wsProtocol = if (org.scalajs.dom.document.location.protocol == "https:") "wss" else "ws"

    s"$wsProtocol://127.0.0.1:8180/$socketId"
  }

  private def setupRenderer:Unit ={
    renderer.setClearColor(new Color(0x000040),0.5)
    window.addEventListener("resize",
      (e0: Event) => {
        val MARGIN = 0
        val WIDTH = window.innerWidth
        val HEIGHT = window.innerHeight - 2 * MARGIN
        renderer.setSize( WIDTH, HEIGHT )
      }
      ,false
    )
  }
}

// scalastyle: on

/**
  * Just shows that some effects are working
  *
  * @param cam the camera control
  * @param el the html element
  * @param sc scene
  * @param center center of screen
  */
class ExampleControls(cam: Camera, el: HTMLElement, sc: Scene, width: Double, height: Double,
                      center: Vector3 = new Vector3()
                     ) extends JumpCameraControls(cam, el, sc, width, height, center) {
  import org.querki.jquery._

  lazy val $el = $(el)

  override def onMouseMove(event: MouseEvent) = {
    val (offsetX, offsetY) = ($el.offset().left, $el.offset().top)
    this.onCursorMove(event.clientX - offsetX, event.clientY - offsetY, width, height)

    enter.keys.foreach {
      case m: Mesh =>
        m.material match {
          case mat: MeshLambertMaterial => mat.wireframe = true
          case _ => // do nothing
        }

      case _ => // do nothing
    }

    exit.keys.foreach {
      case m: Mesh =>
        m.material match {
          case mat: MeshLambertMaterial => mat.wireframe = false
          case _ => // do nothing
        }

      case _ => // do nothing
    }

    rotateOnMove(event)

  }

}

trait ExampleData {

  lazy val myCode =
    """
      |object Example
      |{
      |  def activate(): Unit = {
      |    //get html container for 3d scence
      |    val el:HTMLElement = dom.document.getElementById("container").asInstanceOf[HTMLElement]
      |    val demo = new ExampleScene(el,1280,768) //set the size of the canvas
      |    demo.render() //render
      |  }
      |}
      |
      |class ExampleScene(val container:HTMLElement, val width:Double, val height:Double) extends Container3D
      |{
      |  val geometry = new BoxGeometry( 350, 300, 250 )
      |  val colors = List("green","red","blue","orange","purple","teal")
      |  val colorMap= Map("green"->0xA1CF64,"red"->0xD95C5C,"blue" -> 0x6ECFF5,"orange" ->0xF05940,"purple"->0x564F8A,"teal"->0x00B5AD)
      |
      |  def materialParams(name:String) = js.Dynamic.literal(
      |    color = new Color(colorMap(name))
      |   // wireframe = true
      |  ).asInstanceOf[MeshLambertMaterialParameters]
      |
      |  def randColorName = colors(Random.nextInt(colors.size))
      |
      |  protected def nodeTagFromTitle(title:String,colorName:String) =  textarea(title,`class`:=s"ui large ${colorName} message").render
      |
      |  var meshes = addMesh(new Vector3(0,0,0))::addMesh(new Vector3(400,0,200))::addMesh(new Vector3(-400,0,200))::Nil //test meshes
      |
      |  var sprites = List.empty[HtmlSprite] //test sprite
      |
      |  override val controls:CameraControls = new ExampleControls(camera,this.container,scene,width,height,this.meshes.head.position.clone())
      |
      |  val light = new DirectionalLight( 0xffffff, 2 )
      |  light.position.set( 1, 1, 1 ).normalize()
      |  scene.add( light )
      |
      |  def addMesh(pos:Vector3 = new Vector3()) = {
      |    val material = new MeshLambertMaterial( this.materialParams(randColorName) )
      |    val mesh: Mesh = new Mesh( geometry, material )
      |    mesh.name = pos.toString
      |    mesh.position.set(pos.x,pos.y,pos.z)
      |    mesh
      |  }
      |
      |  def addLabel(pos:Vector3,title:String = "hello three.js and ScalaJS!") = {
      |    val helloHtml = nodeTagFromTitle(title,randColorName)
      |    val html = new HtmlSprite(helloHtml)
      |    html.position.set(pos.x,pos.y,pos.z)
      |    html
      |  }
      |
      |  meshes.foreach(scene.add)
      |  meshes.zipWithIndex.foreach{case (m,i)=>
      |    this.sprites =  addLabel(m.position.clone().setY(m.position.y+200),"Text #"+i)::this.sprites //for each box add label on top
      |  }
      |  sprites.foreach(cssScene.add) //we have separate scence for Sprites and other html elements in 3D
      |
      |}
    """.stripMargin

  lazy val htmlCode = """<section class="ui blue pilled message">
                        |    ScalaJS interface for THREE.js javascript lib. You can rotate the cube and sprite by holding left mouse button
                        |</section>
                        |<section id="container">
                        |""".stripMargin

}