name := "CienaML"

version := "1.0"

scalaVersion := "2.10.4"


retrieveManaged := true

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.1" % "provided",
  "org.apache.spark" %% "spark-mllib" % "1.6.1" % "provided",
  "com.holdenkarau" %% "spark-testing-base" % "1.6.1_0.3.2",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)



assemblyMergeStrategy in assembly := {
  case PathList("org", "apache", xs @ _*) => MergeStrategy.last
  case PathList("org", "scalatest", xs @ _*) => MergeStrategy.last
  case PathList("com", "holdenkarau", xs @ _*) => MergeStrategy.last
  case PathList("cc", "factorie", xs @ _*) => MergeStrategy.last
  case "about.html" => MergeStrategy.rename
  case "META-INF/ECLIPSEF.RSA" => MergeStrategy.last
  case "META-INF/mailcap" => MergeStrategy.last
  case "META-INF/mimetypes.default" => MergeStrategy.last
  case "plugin.properties" => MergeStrategy.last
  case "log4j.properties" => MergeStrategy.last
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}