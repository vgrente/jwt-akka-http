val scalaV = "2.12.4"

name         := "jwt-akka-http"
organization := "com.emarsys"
version      := "0.1.1"

scalaVersion := scalaV
scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-language:higherKinds",
  "-Ywarn-dead-code",
  "-Xfatal-warnings",
  "-Xlint"
)

libraryDependencies ++= {
  val akkaHttpV  = "10.0.10"
  val scalaTestV = "3.0.4"
  Seq(
    "com.typesafe.akka" %% "akka-http"            % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "org.scalatest"     %% "scalatest"            % scalaTestV % "test",
    "com.pauldijou"     %% "jwt-core"             % "0.14.1",
    "com.github.fommil" %% "spray-json-shapeless" % "1.3.0",
    "org.slf4j"         %  "slf4j-nop"            % "1.7.21"
  )
}

scalaVersion in ThisBuild := scalaV

publishTo := Some(Resolver.file("releases", new File("releases")))
