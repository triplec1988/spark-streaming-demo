import Dependencies._

ThisBuild / scalaVersion     := "2.11.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "spark-streaming-demo",
    libraryDependencies += scalaTest % Test
  )

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-streaming_2.11" % "2.4.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.1.8"
)

/* without this explicit merge strategy code you get a lot of noise from sbt-assembly
   complaining about not being able to dedup files */
assemblyMergeStrategy in assembly := {
  case PathList("org","aopalliance", xs @ _*) => MergeStrategy.last
  case PathList("javax", "inject", xs @ _*) => MergeStrategy.last
  case PathList("javax", "servlet", xs @ _*) => MergeStrategy.last
  case PathList("javax", "activation", xs @ _*) => MergeStrategy.last
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("com", "google", xs @ _*) => MergeStrategy.last
  case PathList("com", "esotericsoftware", xs @ _*) => MergeStrategy.last
  case PathList("com", "codahale", xs @ _*) => MergeStrategy.last
  case PathList("com", "yammer", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case "overview.html" => MergeStrategy.last  // Added this for 2.1.0 I think
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

/* including scala bloats your assembly jar unnecessarily, and may interfere with
   spark runtime */
assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)

/* you need to be able to undo the "provided" annotation on the deps when running your spark
   programs locally i.e. from sbt; this bit reincludes the full classpaths in the compile and run tasks. */
fullClasspath in Runtime := (fullClasspath in (Compile, run)).value