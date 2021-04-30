package de.htwg.se.tiles

import de.htwg.se.tiles.control.Controller
import de.htwg.se.tiles.view.tui.Tui

import scala.io.StdIn.readLine

object Game {
	def main(args: Array[String]): Unit = {
		var input: String = ""
		var tui = new Tui(new Controller(), 120, 30, 5, (0, 0))
		var msg = Option.empty[String]

		do {
			input = readLine()
			msg = tui.command(input)
		} while (input != "exit")
	}
}
