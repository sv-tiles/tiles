package de.htwg.se.tiles.model

import scala.collection.immutable.HashMap

case class Map(tiles: HashMap[(Int, Int), Tile] = new HashMap[(Int, Int), Tile]()) {
	def add(pos: (Int, Int), tile: Tile): Map = {
		if (tiles.contains(pos)) {
			throw PlacementException("")
		}
		copy(tiles.updated(pos, tile))
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
		toString(off, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, false)
	}

	def toString(offset: (Int, Int), mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int, frame: Boolean = true, highlight: Option[(Int, Int)] = Option.empty): String = {
		require(mapWidth > 0)
		require(mapHeight > 0)
		if (highlight.isDefined) {
			require(margin > 0)
		}

		val rows = Math.ceil(mapHeight / (tileHeight + margin / 2).doubleValue + offset._2).intValue
		val cols = Math.ceil(mapWidth / (tileWidth + margin).doubleValue + offset._1).intValue

		val lines = for (y <- offset._2 until rows; line <- 0 until tileHeight + margin / 2) yield
			(for (x <- offset._1 until cols) yield if (tiles.contains(x, y)) {
				if (line < tileHeight && highlight.isDefined && highlight.get == (x + 1, y)) {
					this.tiles(x, y).printLine(line, tileWidth, tileHeight, border, 0) + " " * (margin - 1) + ">"
				} else if (line < tileHeight && highlight.isDefined && highlight.get == (x, y)) {
					this.tiles(x, y).printLine(line, tileWidth, tileHeight, border, 0) + "<" + " " * (margin - 1)
				} else {
					this.tiles(x, y).printLine(line, tileWidth, tileHeight, border, margin)
				}
			} else {
				if (line < tileHeight && highlight.isDefined && highlight.get == (x + 1, y)) {
					" " * tileWidth + " " * (margin - 1) + ">"
				} else if (line < tileHeight && highlight.isDefined && highlight.get == (x, y)) {
					" " * tileWidth + "<" + " " * (margin - 1)
				} else {
					" " * (tileWidth + margin)
				}
			}).mkString.substring(0, mapWidth)
		val text = (if (frame) lines.map(l => "|" + l + "|\n") else lines.map(l => l + "\n"))
			.grouped(mapHeight).next().mkString

		(if (frame) "+" + "-" * mapWidth + "+\n" else "") + text + (if (frame) "+" + "-" * mapWidth + "+" else "")
	}
}
