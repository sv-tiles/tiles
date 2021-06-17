package de.htwg.se.tiles

import com.google.inject.Guice
import de.htwg.se.tiles.control.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.tiles.view.gui.Gui
import de.htwg.se.tiles.view.tui.Tui

import scala.io.StdIn.readLine

object Game {
	def main(args: Array[String]): Unit = {
		val test = args.exists(s => s.equals("--test"))
		val testModule = args.exists(s => s.equals("--test-module"))

		val injector = Guice.createInjector(if (testModule) TestModule() else GameModule())

		var input: String = ""
		val controller = injector.getInstance(classOf[Controller])
		val tui = new Tui(controller, 120, 30, 5, (0, 0))
		val gui = new Gui(controller)
		gui.jumpstart(test)
		do {
			input = readLine()
			tui.command(input)
		} while (input != "exit")
	}
}
