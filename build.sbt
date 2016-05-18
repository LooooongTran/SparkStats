name := "SparkStats"

version := "1.0"

scalaVersion := "2.10.4"


retrieveManaged := true

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.1" % "provided",
  "org.apache.spark" %% "spark-mllib" % "1.6.1" % "provided",
  "com.holdenkarau" %% "spark-testing-base" % "1.6.1_0.3.2",
  "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

