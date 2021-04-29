package de.htwg.se.tiles.view.tui

import de.htwg.se.tiles.control.Controller

import scala.util.Try

case class Tui(controller: Controller, width: Int, height: Int, scale: Int, offset: (Int, Int), highlight: Option[(Int, Int)]) {
	require(scale >= 3)
	require(width >= 1)
	require(height >= 1)

	def command(command: String): (Tui, Option[String]) = command.replaceAll("\\W+", " ").trim() match {
		case "w" => (this.copy(offset = offset.copy(_2 = offset._2 - 1)), Option.empty)
		case "a" => (this.copy(offset = offset.copy(_1 = offset._1 - 1)), Option.empty)
		case "s" => (this.copy(offset = offset.copy(_2 = offset._2 + 1)), Option.empty)
		case "d" => (this.copy(offset = offset.copy(_1 = offset._1 + 1)), Option.empty)
		case s"position $x $y" if Try(x.toInt).isSuccess && Try(y.toInt).isSuccess =>
			(this.copy(offset = (x.toInt, y.toInt)), Option.empty)
		case "position" => (this, Option("Position: " + offset._1 + " " + offset._2))
		case s"scale $f" if Try(f.toInt).isSuccess => (this.copy(scale = f.toInt), Option.empty)
		case "scale" => (this, Option("Scale: " + scale))
		case s"width $width" if Try(width.toInt).isSuccess => (this.copy(width = width.toInt), Option.empty)
		case "width" => (this, Option("Width: " + width))
		case s"height $height" if Try(height.toInt).isSuccess => (this.copy(height = height.toInt), Option.empty)
		case "height" => (this, Option("Height: " + height))
		case s"size $width $height" if Try(width.toInt).isSuccess && Try(height.toInt).isSuccess =>
			(this.copy(width = width.toInt, height = height.toInt), Option.empty)
		case "size" => (this, Option("Size: " + width + " " + height))
		case s"highlight $x $y" if Try(x.toInt).isSuccess && Try(y.toInt).isSuccess =>
			(this.copy(highlight = Option((x.toInt, y.toInt))), Option.empty)
		case "highlight none" => (this.copy(highlight = Option.empty), Option.empty)
		case "highlight" => (this, Option("Highlight: " + (if (highlight.isDefined) highlight.get._1 + " " + highlight.get._2 else "none")))
		case "exit" => (this, Option("stopping"))


		case _ => (this, Option("Unknown command: " + command))
	}

	def getView: String =
		controller.mapToString(offset, width, height, scale * 2, scale, Math.max(1, scale * .2).intValue, 2, frame = true, highlight) +
			"\n" + controller.currentTileToString(scale * 2, scale, Math.max(1, scale * .2).intValue, 2).trim() + "\n\n"
}