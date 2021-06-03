package de.htwg.se.tiles

import de.htwg.se.tiles.control.Controller
import de.htwg.se.tiles.model.rules.BasicRules
import de.htwg.se.tiles.view.gui.Gui
import de.htwg.se.tiles.view.tui.Tui

import scala.io.StdIn.readLine

object Game {
	def main(args: Array[String]): Unit = {
		val test = args.exists(s => s.equals("--test"))
		var input: String = ""
		val controller = new Controller(rules = BasicRules())
		val tui = new Tui(controller, 120, 30, 5, (0, 0))
		if (!test) {
			val gui = new Gui(controller)
			val guiThread = new Thread(() => {
				gui.main(Array.empty);
				System.exit(0)
			})
			guiThread.setDaemon(true)
			guiThread.start()
		}
		do {
			input = readLine()
			tui.command(input)
		} while (input != "exit")
	}
}
