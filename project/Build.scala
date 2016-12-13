import sbt._
import Keys._


object ShapelessBuilderBuild extends Build {

  lazy val shapelessBuilder = (project in file(".")
    aggregate(core, examples)
    dependsOn(core, examples)
    settings(commonSettings ++ doNotPublishSettings: _*)
    settings(
      moduleName := "shapeless-builder-root"
    )
  )

  lazy val core = (project
    settings(commonSettings ++ publishSettings: _*)
    settings(
      name := "shapeless-builder"
    )
//    settings(
//      publishTo <<= version { (v: String) =>
//        val disposition = if ( v.trim.endsWith("SNAPSHOT") ) "snapshots" else "releases"
//        Some( Resolver.file("file", new File( Path.userHome.absolutePath + s"/jd/dev/dmrolfs.github.com/${disposition}" ) ) )
//        // if ( v.trim.endsWith("SNAPSHOT") ) Some( "snapshots" at nexus + "snapshots" )
//        // else Some( "releases" at nexus + "releases" )
//        // val nexus = "http://utility.allenai.org:8081/nexus/content/repositories/"
//        // val nexus = "http://utility.allenai.org:8081/nexus/content/repositories/"
//      }
//    )
  )


  lazy val examples = (project
    dependsOn core
    settings(commonSettings ++ doNotPublishSettings: _*)
    settings(
      moduleName := "shapeless-builder-examples"
    )
  )

  def commonSettings = 
    Seq(
      organization := "com.github.dmrolfs",
      version := "1.0.0",
      scalaVersion := "2.11.7",
      scalacOptions := Seq(
          "-feature",
          "-language:higherKinds",
          "-Xfatal-warnings",
          // "-Xlog-implicits",
          // "-Xprint:typer",
          "-deprecation",
          "-unchecked"
      ),

      resolvers ++= Seq(
        Resolver.sonatypeRepo("releases"),
        Resolver.sonatypeRepo("snapshots")
      ),

      resolvers += Resolver.jcenterRepo,

      libraryDependencies ++= Seq(
        "com.chuusai" %% "shapeless" % "2.2.5" withSources(),
        "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
      )
    )

  def doNotPublishSettings = Seq(publish := {})

  def publishSettings = if ( (version in ThisBuild).toString.endsWith("-SNAPSHOT") ) {
    Seq(
         publishTo := Some("Artifactory Realm" at "http://oss.jfrog.org/artifactory/oss-snapshot-local"),
         // Only setting the credentials file if it exists (#52)
         credentials := List(Path.userHome / ".bintray" / ".artifactory").filter(_.exists).map(Credentials(_))
       )
  } else {
    Seq(
      pomExtra := <scm>
        <url>https://github.com</url>
        <connection>https://github.com/dmrolfs/shapeless-builder.git</connection>
      </scm>
      <developers>
        <developer>
          <id>dmrolfs</id>
          <name>Damon Rolfs</name>
          <url>http://dmrolfs.github.io/</url>
        </developer>
      </developers>,
      publishMavenStyle := true,
      resolvers += Resolver.url("omen bintray resolver", url("http://dl.bintray.com/omen/maven"))(Resolver.ivyStylePatterns),
      licenses := ("MIT", url("http://opensource.org/licenses/MIT")) :: Nil // this is required! otherwise Bintray will reject the code
    )
  }
}
