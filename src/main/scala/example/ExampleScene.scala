package example

import objects3d.{LayoutCurve, SignedBox, Label, Actors}
import org.denigma.threejs.extensions.controls.{JumpCameraControls, CameraControls}
import org.denigma.threejs._
import org.denigma.threejs.extensions.Container3D
import org.scalajs.dom
import org.scalajs.dom._
import org.scalajs.dom.raw.HTMLElement
import polyverse.{DockerContainerData, Cluster}
import upickle.default._
import com.scalawarrior.scalajs.createjs.{Ease, Tween}

import scala.scalajs.js

// scalastyle:off
class ExampleScene(val container: HTMLElement, val width: Double, val height: Double) extends Container3D {


  camera.position.x = -250
  camera.position.y = 250
  camera.position.z = -1400
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
  meshes foreach (scene.add)

  val datasource = new WebSocket(getWebsocketUri(org.scalajs.dom.document, "status"))

  var clusterState = Cluster(Seq(), Seq(), Seq())
  var containersInScene = Map[String, Object3D]()

  datasource.onmessage = (event: MessageEvent) => handleContainersUpdate(event)

  private def handleContainersUpdate(event: MessageEvent): Unit = {

    dom.console.log(event.data.toString)

    val priorClusterState = clusterState
    // TODO: Is State monad applicable here?
    priorClusterState.priorClusterState = Cluster.emptyClusterState // Drop old history preventing heap exhaustion
    clusterState = Cluster(event.data.toString)
    clusterState.priorClusterState = priorClusterState

    // Clear old ones from the scene
    clusterState.removedContainers map {
      containersInScene(_)
    } foreach scene.remove
    containersInScene = containersInScene -- clusterState.removedContainers

    // Introduce new ones
    val newContainersMeshes = createContainerSetMeshes(new Vector3(0, 0, 0), clusterState.newContainers)
    newContainersMeshes.values foreach scene.add
    containersInScene = containersInScene ++ newContainersMeshes

    positionMeshes
  }



  private def positionMeshSet(containerSet: () => Set[String], layoutStart: Vector3): Unit = {
    val warmingUp = containerSet() map {
      containersInScene(_)
    } zipWithIndex
    val r = warmingUp foreach { c =>
      val pos = ContainerMeshLocation(layoutStart, c._2)
      val pl = js.Dynamic.literal(x = pos.x, y = pos.y, z = pos.z)
      val box = c._1
      Tween.get(box.position).wait(c._2 * 150, false).to(pl, 3000, Ease.getPowInOut(4)).onChange = () => {
        box.position.set(box.position.x, box.position.y, box.position.z)
      }
    }
  }

  private def positionMeshes: Unit = {
    positionMeshSet(clusterState.systemContainers _, new Vector3(0,200,200))
    positionMeshSet(clusterState.warmingUpContainers _, new Vector3(-200,10,200))
    positionMeshSet(clusterState.servingContainers _, new Vector3(0,10,200))
    positionMeshSet(clusterState.garbageContainers _, new Vector3(200,10,200))
  }

  def createContainerSetMeshes(curveStart: Vector3, containers: Set[String]): Map[String, Object3D] = {
    containers.zipWithIndex map { t =>
      val box = SignedBox(t._1)
      box.position.set(5500, 1400, -20000)
      val p = ContainerMeshLocation(new Vector3(), t._2)
      val pl = js.Dynamic.literal(x = p.x, y = p.y, z = p.z)
      Tween.get(box.position).wait(t._2 * 150, false).to(pl, 3000, Ease.getPowInOut(4)).onChange = () => {
        box.position.set(box.position.x, box.position.y, box.position.z)
      }
      (t._1, box)
    } toMap
  }

  def ContainerMeshLocation(curveStart: Vector3, i: Int): Vector3 = {
    curveStart.add(LayoutCurve.grid(i))
  }


  private def getWebsocketUri(document: Document, socketId: String): String = {
    val wsProtocol = if (org.scalajs.dom.document.location.protocol == "https:") "wss" else "ws"

    s"$wsProtocol://127.0.0.1:8180/$socketId"
  }

  private def setupRenderer: Unit = {
    renderer.setClearColor(new Color(0x000040), 0.5)
    window.addEventListener("resize",
      (e0: Event) => {
        val MARGIN = 0
        val WIDTH = window.innerWidth
        val HEIGHT = window.innerHeight - 2 * MARGIN
        renderer.setSize(WIDTH, HEIGHT)
      }
      , false
    )
  }
}

// scalastyle: on

/**
  * Just shows that some effects are working
  *
  * @param cam    the camera control
  * @param el     the html element
  * @param sc     scene
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

  lazy val htmlCode =
    """<section class="ui blue pilled message">
      |    ScalaJS interface for THREE.js javascript lib. You can rotate the cube and sprite by holding left mouse button
      |</section>
      |<section id="container">
      | """.stripMargin

}