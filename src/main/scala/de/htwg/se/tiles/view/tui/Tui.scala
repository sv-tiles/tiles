package de.htwg.se.tiles.view.tui

import de.htwg.se.tiles.control.Controller

import scala.util.Try

class Tui(controller: Controller, var width: Int, var height: Int, var scale: Int, var offset: (Int, Int)) {
	require(scale >= 3)
	require(width >= 1)
	require(height >= 1)

	var cursor: (Int, Int) = (0, 0)

	def command(command: String): Option[String] = {
		var result = Option.empty[String]

		command.replaceAll("\\W+", " ").trim() match {
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

			case "clear" => controller.clear()
			case "place" => controller.commit()
			case "t" =>
				cursor = (cursor._1, cursor._2 - 1)
				if (!controller.placeTile(cursor)._1) {
					result = Option("OCCUPIED")
				}
			case "g" =>
				cursor = (cursor._1, cursor._2 + 1)
				if (!controller.placeTile(cursor)._1) {
					result = Option("OCCUPIED")
				}
			case "f" =>
				cursor = (cursor._1 - 1, cursor._2)
				if (!controller.placeTile(cursor)._1) {
					result = Option("OCCUPIED")
				}
			case "h" =>
				cursor = (cursor._1 + 1, cursor._2)
				if (!controller.placeTile(cursor)._1) {
					result = Option("OCCUPIED")
				}
			case "q" | "r" =>
				controller.rotate(false)
			case "e" | "z" =>
				controller.rotate(true)
			case "cursor reset" =>
				cursor = offset
				if (!controller.placeTile(cursor)._1) {
					result = Option("OCCUPIED")
				}

			case _ => result = Option("Unknown command: " + command)
		}
		result
	}

	def getView: String =
		controller.mapToString(offset, width, height, scale * 2, scale, Math.max(1, scale * .2).intValue, 2, frame = true, Option(cursor)) +
			"\n" + controller.currentTileToString(scale * 2, scale, Math.max(1, scale * .2).intValue, 2).trim() + "\n\n"

}
