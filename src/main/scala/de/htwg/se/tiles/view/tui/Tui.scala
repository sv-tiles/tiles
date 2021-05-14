package de.htwg.se.tiles.view.tui

import de.htwg.se.tiles.control.Controller
import de.htwg.se.tiles.util.Observer

import scala.util.Try

class Tui(controller: Controller, var width: Int, var height: Int, var scale: Int, var offset: (Int, Int)) extends Observer[(Boolean, String)] {
	require(scale >= 3)
	require(width >= 1)
	require(height >= 1)
	controller.add(this)


	var cursor: (Int, Int) = (0, 0)
	var result = Option.empty[String]

	private val commandHandlers: CommandHandler = (command: String) => if (command == "clear") {
		controller.clear()
		true
	} else false

	commandHandlers.appendHandler((command: String) => if (command == "place" || command == "u") {
		controller.commit()
		true
	} else false).appendHandler((command: String) => if (command == "t") {
		cursor = (cursor._1, cursor._2 - 1)
		controller.placeTile(cursor)
		true
	} else false).appendHandler((command: String) => if (command == "g") {
		cursor = (cursor._1, cursor._2 + 1)
		controller.placeTile(cursor)
		true
	} else false).appendHandler((command: String) => if (command == "f") {
		cursor = (cursor._1 - 1, cursor._2)
		controller.placeTile(cursor)
		true
	} else false).appendHandler((command: String) => if (command == "h") {
		cursor = (cursor._1 + 1, cursor._2)
		controller.placeTile(cursor)
		true
	} else false).appendHandler((command: String) => if (command == "q" || command == "r") {
		controller.rotate(false)
		true
	} else false).appendHandler((command: String) => if (command == "e" || command == "z") {
		controller.rotate(true)
		true
	} else false).appendHandler((command: String) => if (command == "cursor reset") {
		cursor = offset
		controller.placeTile(cursor)
		true
	} else false)

	update((true, ""))

	def command(command: String): Option[String] = {
		result = Option.empty[String]

		val cmd = command.replaceAll("\\W+", " ").trim()
		if (!commandHandlers.handleCommand(cmd)) {
			cmd match {
				case "w" => offset = offset.copy(_2 = offset._2 - 1)
				case "a" => offset = offset.copy(_1 = offset._1 - 1)
				case "s" => offset = offset.copy(_2 = offset._2 + 1)
				case "d" => offset = offset.copy(_1 = offset._1 + 1)
				case s"position $x $y" if Try(x.toInt).isSuccess && Try(y.toInt).isSuccess =>
					offset = (x.toInt, y.toInt)
				case "position" => result = Option("Position: " + offset._1 + " " + offset._2)
				case s"scale $f" if Try(f.toInt).isSuccess => scale = f.toInt
				case "scale" => result = Option("Scale: " + scale)
				case s"width $width" if Try(width.toInt).isSuccess => this.width = width.toInt
				case "width" => result = Option("Width: " + width)
				case s"height $height" if Try(height.toInt).isSuccess => this.height = height.toInt
				case "height" => result = Option("Height: " + height)
				case s"size $width $height" if Try(width.toInt).isSuccess && Try(height.toInt).isSuccess =>
					this.width = width.toInt
					this.height = height.toInt
				case "size" => result = Option("Size: " + width + " " + height)
				case "exit" => result = Option("stopping")
				case _ => result = Option("Unknown command: " + command)
			}
			update((true, ""))
		}


		result
	}

	override def update(value: (Boolean, String)): Unit = {
		print("\u001b[2J")
		print(Console.BOLD)
		print(Console.GREEN)

		if (value._2 == "Already occupied") {
			println(getView)
		} else {
			println(getView.split("\n").map(l => """>.*<|^[^>]*<|>[^<]*$""".r.replaceAllIn(l, m => Console.RED + m.matched + Console.GREEN)).mkString("\n"))
		}
		print(Console.RESET)
		print(Console.UNDERLINED)
		if (result.isDefined) {
			println(result.get)
		}
		if (value._2.nonEmpty) {
			println(value._2)
		}
		print(Console.RESET)
	}


	def getView: String =
		controller.mapToString(offset, width, height, scale * 2, scale, Math.max(1, scale * .2).intValue, 2, frame = true, Option(cursor)) +
			"\n" + controller.currentTileToString(scale * 2, scale, Math.max(1, scale * .2).intValue, 2).trim() + "\n\n"

}
