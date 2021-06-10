package de.htwg.se.tiles.model.boardComponent.boardBaseImpl

import de.htwg.se.tiles.model.Direction
import de.htwg.se.tiles.model.boardComponent.{Terrain, TileInterface}

import scala.util.{Success, Try}

object Tile {
	def apply(all: Terrain): Tile = Tile(all, all, all, all, all)

	def apply(center: Terrain, border: Terrain): Tile = Tile(border, border, border, border, center)
}

case class Tile(north: Terrain, east: Terrain, south: Terrain, west: Terrain, center: Terrain) extends TileInterface {

	override def rotate(clockwise: Boolean = true): Tile = if (clockwise)
		copy(north = west, east = north, south = east, west = south) else
		copy(north = east, east = south, south = west, west = north)

	override def printLine(line: Int, width: Int, height: Int, border: Int, margin: Int): Try[String] = Try {
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

	override def tileToString(width: Int, height: Int, border: Int, margin: Int): Try[String] =
		Try((for (i <- 0 until height) yield printLine(i, width, height, border, margin).get).mkString("\n"))

	override def toString: String = center.symbol

	override def getTerrainAt(direction: Direction): Try[Terrain] = direction match {
		case Direction.North => Success(north)
		case Direction.East => Success(east)
		case Direction.South => Success(south)
		case Direction.West => Success(west)
	}

	override def set(north: Terrain, east: Terrain, south: Terrain, west: Terrain, center: Terrain): Tile =
		copy(north, east, south, west, center)
}
