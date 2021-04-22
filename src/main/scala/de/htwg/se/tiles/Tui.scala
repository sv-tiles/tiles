package de.htwg.se.tiles

import scala.util.Try

case class Tui(width: Int, height: Int, scale: Int, offset: (Int, Int), map: Map) {
	require(scale >= 3)
	require(width >= 1)
	require(height >= 1)

	def command(command: String): (Tui, Option[String]) = command match {
		case "w" => (this.copy(offset = offset.copy(_2 = offset._2 - 1)), Option.empty)
		case "a" => (this.copy(offset = offset.copy(_1 = offset._1 - 1)), Option.empty)
		case "s" => (this.copy(offset = offset.copy(_2 = offset._2 + 1)), Option.empty)
		case "d" => (this.copy(offset = offset.copy(_1 = offset._1 + 1)), Option.empty)
		case s"scale $f" if Try(f.toInt).isSuccess => (this.copy(scale = f.toInt), Option.empty)
		case s"width $width" if Try(width.toInt).isSuccess => (this.copy(width = width.toInt), Option.empty)
		case s"height $height" if Try(height.toInt).isSuccess => (this.copy(height = height.toInt), Option.empty)
		case s"rand $x $y" if Try(x.toInt).isSuccess && Try(y.toInt).isSuccess =>
			(copy(map = map.add((x.toInt, y.toInt), Tile.random())), Option.empty)
		case "exit" => (this, Option("stopping"))
		case _ => (this, Option("Unknown command: " + command))
	}

	def getMapView: String =
		map.toString(offset, width, height, scale * 2, scale, Math.max(1, scale * .2).intValue, 2)
}
