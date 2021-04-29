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
			print("\u001b[2J")
			print(Console.BOLD)
			print(Console.GREEN)

			if (msg.isDefined && msg.get == "OCCUPIED") {
				println(tui.getView)
			} else {
				println(tui.getView.split("\n").map(l => """>.*<|^[^>]*<|>[^<]*$""".r.replaceAllIn(l, m => Console.RED + m.matched + Console.GREEN)).mkString("\n"))
			}
			print(Console.RESET)
			print(Console.UNDERLINED)
			if (msg.isDefined) {
				println(msg.get)
			}
			print(Console.RESET)
			input = readLine()
			msg = tui.command(input)
		} while (input != "exit")
	}
}
