name := """lyearnscalacopy"""

version := "0.0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "com.typesafe" % "config" % "1.3.1",
    "com.fasterxml.uuid" % "java-uuid-generator" % "3.1.3",
    "org.tpolecat" %% "doobie-core" % "0.3.0",
    "org.tpolecat" %% "doobie-contrib-postgresql" % "0.3.0",
    "org.tpolecat" %% "doobie-contrib-specs2" % "0.3.0",
    "org.apache.thrift" % "libthrift" % "0.9.3",
    "com.twitter" %% "scrooge-core" % "4.10.0",
    "com.twitter" %% "finagle-thrift" % "6.38.0",
    "org.apache.commons" % "commons-lang3" % "3.4",
    "org.postgresql" % "postgresql" % "9.4.1211.jre7",
    "org.slf4j" % "slf4j-log4j12" % "1.7.21",
    "com.zaxxer" % "HikariCP" % "2.5.1"
)

fork in run := true

enablePlugins(DockerPlugin)

dockerfile in docker := {
  val jarFile: File = sbt.Keys.`package`.in(Compile, packageBin).value
  val classpath = (managedClasspath in Compile).value
  val mainclass = mainClass.in(Compile, packageBin).value.getOrElse(sys.error("Expected exactly one main class"))
  val jarTarget = s"/app/${jarFile.getName}"
  // Make a colon separated classpath with the JAR file
  val classpathString = classpath.files.map("/app/" + _.getName)
    .mkString(":") + ":" + jarTarget
  new Dockerfile {
    // Base image
    from("anapsix/alpine-java")
    // Add all files on the classpath
    add(classpath.files, "/app/")
    // Add the JAR file
    add(jarFile, jarTarget)
    // On launch run Java with the classpath and the main class
    entryPoint("java", "-cp", classpathString, mainclass)
  }
}
