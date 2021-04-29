package de.htwg.se.tiles.model

import scala.util.Random

object Tile {
	def random(seed: Long = System.currentTimeMillis()): Tile = {
		Random.setSeed(seed)
		val arr = Vector() ++ Terrain.defaults
		val max = arr.size
		Tile(arr(Random.nextInt(max)), arr(Random.nextInt(max)), arr(Random.nextInt(max)), arr(Random.nextInt(max)), arr(Random.nextInt(max)))
	}
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
}
