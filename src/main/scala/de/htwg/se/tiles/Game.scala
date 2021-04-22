package de.htwg.se.tiles

import scala.io.StdIn.readLine

object Game {
	def main(args: Array[String]): Unit = {
		var input: String = ""
		var tui = Tui(50, 30, 6, (0, 0), Map())

		do {
			println(tui.map)
			input = readLine()
			tui = tui.command(input)
		} while (input != "exit")
	}
}
