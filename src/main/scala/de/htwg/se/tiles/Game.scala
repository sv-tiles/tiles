package de.htwg.se.tiles

import scala.io.StdIn.readLine

object Game {
	def main(args: Array[String]): Unit = {
		var input: String = ""
		var tui = Tui(120, 30, 5, (0, 0), Map())
		var msg = Option.empty[String]

		do {
			println(tui.getMapView)
			if (msg.isDefined) {
				println(msg.get)
			}
			input = readLine()
			val (newTui, newMsg) = tui.command(input)
			tui = newTui
			msg = newMsg
		} while (input != "exit")
	}
}
