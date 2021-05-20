package de.htwg.se.tiles.model

import de.htwg.se.tiles.model.Direction.{East, North, South, West}

import scala.util.{Failure, Success, Try}

object Tile {
	def apply(all: Terrain): Tile = Tile(all, all, all, all, all)

	def apply(center: Terrain, border: Terrain): Tile = Tile(border, border, border, border, center)
}

case class Tile(north: Terrain, east: Terrain, south: Terrain, west: Terrain, center: Terrain) {

	def rotate(clockwise: Boolean = true): Tile = if (clockwise)
		copy(north = west, east = north, south = east, west = south) else
		copy(north = east, east = south, south = west, west = north)

	@throws[IllegalArgumentException]
	def printLine(line: Int, width: Int, height: Int, border: Int, margin: Int): String = {
		require(line >= 0 && line < height + margin / 2, "line out of bounds")
		require(width >= 3, "width too small")
		require(height >= 3, "height too small")
		require(border > 0, "border has to be >0")
		require(border <= (width - 1) / 2 && border <= (height - 1) / 2, "border too big")
		require(margin >= 0, "margin can not be negative")

		if (line < Math.max(border / 2, 1)) {
			" " * border + north.symbol * (width - (2 * border)) + " " * (border + margin)
		} else if (line >= height) {
			" " * (width + margin)
		} else if (line >= height - Math.max(border / 2, 1)) {
			" " * border + south.symbol * (width - (2 * border)) + " " * (border + margin)
		} else {
			west.symbol * border + center.symbol * (width - (2 * border)) + east.symbol * border + " " * margin
		}
	}

	def tileToString(width: Int, height: Int, border: Int, margin: Int): String =
		(for (i <- 0 until height) yield printLine(i, width, height, border, margin)).mkString("\n")

	override def toString: String = center.symbol

	def getTerrainAt(direction: Direction): Try[Terrain] = direction match {
		case North => Success(north)
		case East => Success(east)
		case South => Success(south)
		case West => Success(west)
		case _ => Failure(new IllegalArgumentException())
	}
}
