package de.htwg.se.tiles

import com.google.inject.Guice
import de.htwg.se.tiles.control.controllerComponent.ControllerInterface
import de.htwg.se.tiles.view.gui.Gui
import de.htwg.se.tiles.view.tui.Tui

import scala.io.StdIn.readLine

object Game {
	def main(args: Array[String]): Unit = {
		val injector = Guice.createInjector(GameModule())

		var input: String = ""
		val controller = injector.getInstance(classOf[ControllerInterface])

		val gui = new Gui(controller)
		val guiThread = new Thread(() => {
			gui.main(Array.empty)
			System.exit(0)
		})
		guiThread.setDaemon(true)
		guiThread.start()

		val tui = new Tui(controller, 120, 30, 5, (0, 0))
		do {
			input = readLine()
			tui.command(input)
		} while (input != "exit")
	}
}
