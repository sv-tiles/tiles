package de.htwg.se.tiles

import scala.collection.immutable.HashMap

case class Map(tiles: HashMap[(Int, Int), Tile] = new HashMap[(Int, Int), Tile]()) {
	def add(pos: (Int, Int), tile: Tile): Map = {
		if (tiles.contains(pos)) {
			throw new PlacementException("")
		}
		return copy(tiles.updated(pos, tile))
	}

	override def toString: String = {
		if (tiles.isEmpty) {
			return "empty"
		}
		val mapWidth = 50
		val mapHeight = 25
		val tileHeight = 5
		val tileWidth = 12
		val border = 2
		val margin = 2

		val off = tiles.keySet.reduce((a, b) => (a._1 min b._1, a._2 min b._2))
		toString(off, mapWidth, mapHeight, tileWidth, tileHeight, border, margin)
	}

	def toString(offset: (Int, Int), mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int): String = {
		require(mapWidth > 0)
		require(mapHeight > 0)

		val rows = mapHeight / (tileHeight + margin / 2) + offset._2
		val cols = mapWidth / (tileWidth + margin) + offset._1

		(for (y <- offset._2 until cols; line <- 0 until tileHeight + margin / 2) yield
			(for (x <- offset._1 until rows) yield (
				if (tiles.contains(x, y))
					this.tiles(x, y).printLine(line, tileWidth, tileHeight, border, margin)
				else
					" " * (tileWidth + margin)
				)).mkString.substring(0, mapWidth) + "\n"
			).grouped(mapHeight).next().mkString
	}
}
