name := """lyearnscalacopy"""

version := "0.0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.2.4" % "test",
    "com.websudos" %% "phantom-dsl" % "1.28.15",
    "com.typesafe" % "config" % "1.3.1",
    "net.liftweb" %% "lift-json" % "3.0-M8"

)

resolvers ++= Seq(
  "Java.net Maven2 Repository"       at "http://download.java.net/maven/2/",
  "Twitter Repository"               at "http://maven.twttr.com",
  "Typesafe Repo" 					 at "http://repo.typesafe.com/typesafe/releases/",
  "Maven central" 					 at "http://repo1.maven.org/maven2/",
  "Compass Repository" 				 at "http://repo.compass-project.org",
  Resolver.sonatypeRepo("releases"),
  Resolver.bintrayRepo("websudos", "oss-releases")

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
