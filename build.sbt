name := "tiles"
organization := "de.htwg.se.tiles"
version := "0.1"
scalaVersion := "2.13.5"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.5" % "test"
libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R24"

libraryDependencies += "com.google.inject" % "guice" % "5.0.1"
libraryDependencies += "com.google.inject.extensions" % "guice-assistedinject" % "5.0.1"
libraryDependencies += "net.codingwell" %% "scala-guice" % "5.0.1"

lazy val javaFXModules = {
	// Determine OS version of JavaFX binaries
	lazy val osName = System.getProperty("os.name") match {
		case n if n.startsWith("Linux") => "linux"
		case n if n.startsWith("Mac") => "mac"
		case n if n.startsWith("Windows") => "win"
		case _ =>
			throw new Exception("Unknown platform!")
	}
	// Create dependencies for JavaFX modules
	Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
		.map(m => "org.openjfx" % s"javafx-$m" % "16" classifier osName)
}

libraryDependencies ++= javaFXModules

libraryDependencies += "org.scala-lang.modules" %% "scala-xml" % "2.0.0"
libraryDependencies += "com.typesafe.play" %% "play-json" % "2.9.2"

Compile / resourceDirectory := baseDirectory.value / "src/main/resources"

coverageExcludedPackages := "de\\.htwg\\.se\\.tiles\\.view\\.gui;.*Game"
