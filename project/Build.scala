import sbt._
import Keys._

object ShapelessBuilderBuild extends Build {

  lazy val shapelessBuilder = (project in file(".")
    aggregate(core, examples)
    dependsOn(core, examples)
    settings(commonSettings: _*)
    settings(
      moduleName := "shapeless-builder-root"
    )
  )

  lazy val core = (project
    settings(commonSettings: _*)
    settings(
      moduleName := "shapeless-builder"
    )
    settings(
      publishTo <<= version { (v: String) =>
        val disposition = if ( v.trim.endsWith("SNAPSHOT") ) "snapshots" else "releases"
        Some( Resolver.file("file", new File( Path.userHome.absolutePath + s"/jd/dev/dmrolfs.github.com/${disposition}" ) ) )
        // if ( v.trim.endsWith("SNAPSHOT") ) Some( "snapshots" at nexus + "snapshots" )
        // else Some( "releases" at nexus + "releases" )
        // val nexus = "http://utility.allenai.org:8081/nexus/content/repositories/"
        // val nexus = "http://utility.allenai.org:8081/nexus/content/repositories/"
      }
    )
  )


  lazy val examples = (project
    dependsOn core
    settings(commonSettings: _*)
    settings(
      moduleName := "shapeless-builder-examples"
    )
  )

  def commonSettings = 
    Seq(
      organization := "com.github.dmrolfs",
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
 
      libraryDependencies ++= Seq(
        "com.chuusai" %% "shapeless" % "2.2.5" withSources(),
        "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
      )
    )
}
