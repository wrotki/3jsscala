import com.lihaoyi.workbench.Plugin._

enablePlugins(ScalaJSPlugin)

workbenchSettings

name := "Example"

version := "0.1-SNAPSHOT"

scalaVersion := "2.11.7"

resolvers += sbt.Resolver.bintrayRepo("denigma", "denigma-releases") //add resolver

resolvers += "amateras-repo" at "http://amateras.sourceforge.jp/mvn/"

libraryDependencies ++= Seq(
  "org.denigma" %%% "threejs-facade" % "0.0.74-0.1.7",
  "org.querki" %%% "jquery-facade" % "1.0-RC6", //scalajs facade for jQuery + jQuery extensions
  "org.querki" %%% "querki-jsext" % "0.7", //useful scalajs extensions
  "org.scala-js" %%% "scalajs-dom" % "0.8.2",
  "com.lihaoyi" %%% "scalatags" % "0.5.4",
  "com.lihaoyi" %%% "upickle" % "0.4.1",
  "com.scalawarrior" %%% "scalajs-createjs" % "0.0.2",
  "org.webjars" % "three.js" % "r77"
)
lazy val datasource = (project in file("datasource"))

lazy val root =(project in file(".")).
  aggregate(datasource)


bootSnippet := "example.ScalaJSExample().main(document.getElementById('canvas'));"

updateBrowsers <<= updateBrowsers.triggeredBy(fastOptJS in Compile)

