package de.htwg.se.tiles

import scala.io.StdIn.readLine

object Game {
	def main(args: Array[String]): Unit = {
		var input: String = ""
		var tui = Tui(120, 30, 5, (0, 0), Map(), Option.empty)
		var msg = Option.empty[String]

		do {
			print("\u001b[2J")
			print(Console.BOLD)
			print(Console.GREEN)

			println(tui.getMapView.split("\n").map(l => """>.*<|^[^>]*<|>[^<]*$""".r.replaceAllIn(l, m => Console.RED + m.matched + Console.GREEN)).mkString("\n"))
			print(tui.getMapView)
			print(Console.RESET)
			print(Console.UNDERLINED)
			if (msg.isDefined) {
				println(msg.get)
			}
			print(Console.RESET)
			input = readLine()
			val (newTui, newMsg) = tui.command(input)
			tui = newTui
			msg = newMsg
		} while (input != "exit")
	}
}
