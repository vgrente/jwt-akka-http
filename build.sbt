name         := "jwt-akka-http"
organization := "com.emarsys"
crossScalaVersions := List("3.0.1", "2.13.6", "2.12.14")

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding", "UTF-8",
  "-unchecked",
  "-feature",
  "-language:implicitConversions",
  "-language:postfixOps",
  "-language:higherKinds",
  "-Xfatal-warnings",

) ++ (CrossVersion.partialVersion(scalaVersion.value) match {
   case Some((2, _)) => Seq(
     "-Xlint",
     "-Ywarn-dead-code"
   )
  case _ => Nil
})

libraryDependencies ++= {
  val akkaV  = "2.6.15"
  val akkaHttpV  = "10.2.6"
  val scalaTestV = "3.2.9"
  Seq(
    ("com.typesafe.akka" %% "akka-http"            % akkaHttpV).cross(CrossVersion.for3Use2_13),
    ("com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpV % Test).cross(CrossVersion.for3Use2_13),
    ("com.typesafe.akka" %% "akka-stream"          % akkaV).cross(CrossVersion.for3Use2_13),
    ("com.typesafe.akka" %% "akka-stream-testkit"  % akkaV % Test).cross(CrossVersion.for3Use2_13),
    ("com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpV).cross(CrossVersion.for3Use2_13),
    "org.scalatest"     %% "scalatest"            % scalaTestV % Test,
    "com.github.jwt-scala"     %% "jwt-core"             % "8.0.3"
  )
}

Global / onChangedBuildSource := ReloadOnSourceChanges

inThisBuild(List(
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  homepage := Some(url("https://github.com/emartech/jwt-akka-http")),
  developers := List(
    Developer("itsdani", "Daniel Segesdi", "daniel.segesdi@emarsys.com", url("https://github.com/itsdani")),
    Developer("doczir", "Robert Doczi", "doczi.r@gmail.com", url("https://github.com/doczir")),
    Developer("tg44", "Gergo Torcsvari", "gergo.torcsvari@emarsys.com", url("https://github.com/tg44")),
    Developer("miklos-martin", "Miklos Martin", "miklos.martin@gmail.com", url("https://github.com/miklos-martin"))
  )
))
