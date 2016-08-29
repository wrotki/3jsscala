organization  := "io.otherbrane"

version       := "0.1"

scalaVersion  := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8")

libraryDependencies ++= {
  val akkaHttpVersion = "2.4.9"
  val akkaV = "2.4.9"
  val sprayV = "1.3.3"
  Seq(
    "io.spray"            %%  "spray-can"     % sprayV withSources() withJavadoc(),
    "io.spray"            %%  "spray-routing" % sprayV withSources() withJavadoc(),
    "io.spray"            %%  "spray-json"    % "1.3.1",
    "io.spray"            %%  "spray-testkit" % sprayV  % "test",
    "com.typesafe.akka"   %%  "akka-actor"    % akkaV,
    "com.typesafe.akka"   %%  "akka-testkit"  % akkaV   % "test",
    "org.specs2"          %%  "specs2-core"   % "2.3.11" % "test",
    "org.scalaz"          %%  "scalaz-core"   % "7.1.0",
    "com.typesafe.akka" %% "akka-stream" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaHttpVersion,
    "org.java-websocket" % "Java-WebSocket" % "1.3.0"
  )
}

fork in run := true
connectInput in run := true

