import scala.util.Properties.envOrElse
val scalaV = "2.12.4"

lazy val commonSettings = Seq(
  scalaVersion := scalaV,
  organization := "com.emarsys"
)

lazy val IntegrationTest = config("it") extend Test
lazy val root = (project in file(".")).
  configs(IntegrationTest).
  settings(commonSettings: _*).
  settings(Defaults.itSettings: _*).
  settings(
    name    := "jwt-authentication",
    version := "0.0.1",

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
    ),

    resolvers ++= Seq(
      "Emarsys Commons" at "https://nexus.service.emarsys.net/repository/emartech" ,
      Resolver.sonatypeRepo("releases")
    ),

    credentials += Credentials(
      "Sonatype Nexus Repository Manager",
      "nexus.service.emarsys.net",
      sys.env("MAVEN_USERNAME"),
      sys.env("MAVEN_PASSWORD")
    ),

    libraryDependencies ++= {
      val akkaHttpV   = "10.0.10"
      val scalaTestV  = "3.0.4"
      Seq(
        "com.typesafe.akka"    %% "akka-http"                 % akkaHttpV,
        "com.typesafe.akka"    %% "akka-http-testkit"         % akkaHttpV,
        "org.scalatest"        %% "scalatest"                 % scalaTestV % "it,test",
        "com.pauldijou"        %% "jwt-core"                  % "0.14.1",
        "com.github.fommil"    %% "spray-json-shapeless"      % "1.3.0",
        "org.slf4j"            %  "slf4j-nop"                 % "1.7.21",
      )
    }
  )

publishTo := Some("releases" at "https://nexus.service.emarsys.net/repository/emartech/")
