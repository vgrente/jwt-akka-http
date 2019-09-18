val scalaV = "2.12.9"

name         := "jwt-akka-http"
organization := "com.emarsys"

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
  val akkaV  = "2.5.25"
  val akkaHttpV  = "10.1.9"
  val scalaTestV = "3.0.4"
  Seq(
    "com.typesafe.akka" %% "akka-http"            % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpV % "test",
    "com.typesafe.akka" %% "akka-stream"          % akkaV,
    "com.typesafe.akka" %% "akka-stream-testkit"  % akkaV % Test,
    "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV,
    "org.scalatest"     %% "scalatest"            % scalaTestV % "test",
    "com.pauldijou"     %% "jwt-core"             % "0.14.1",
    "com.github.fommil" %% "spray-json-shapeless" % "1.4.0"
  )
}

scalaVersion in ThisBuild := scalaV

inThisBuild(List(
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/emartech/jwt-akka-http")),
  developers := List(
    Developer("itsdani", "Daniel Segesdi", "daniel.segesdi@emarsys.com", url("https://github.com/itsdani")),
    Developer("doczir", "Robert Doczi", "doczi.r@gmail.com", url("https://github.com/doczir")),
    Developer("tg44", "Gergo Torcsvari", "gergo.torcsvari@emarsys.com", url("https://github.com/tg44")),
    Developer("miklos-martin", "Miklos Martin", "miklos.martin@gmail.com", url("https://github.com/miklos-martin"))
  ),
  scmInfo := Some(ScmInfo(url("https://github.com/emartech/jwt-akka-http"), "scm:git:git@github.com:emartech/jwt-akka-http.git"))
))
