package de.htwg.se.tiles.model

import scala.collection.immutable.HashMap


// @throws[IllegalArgumentException]
case class Board(tiles: HashMap[Position, Tile] = new HashMap[Position, Tile](), currentTile: Option[Tile] = Option(TileBuilder.randomTile()), currentPos: Option[Position] = Option.empty) {
	require(currentTile.isEmpty ^ currentPos.isEmpty, "current tile XOR current pos! (" + currentTile.isDefined + ", " + currentPos.isDefined + ")")
	require(currentPos.isEmpty || tiles.contains(currentPos.get), "At current pos has to be a tile")

	def rotateCurrentTile(clockwise: Boolean = true): Board = if (currentTile.isDefined)
		copy(currentTile = currentTile.map(t => t.rotate(clockwise))) else
		copy(tiles = tiles.updated(currentPos.get, tiles(currentPos.get).rotate(clockwise)))

	@throws[PlacementException]("Already occupied")
	def placeCurrentTile(pos: Position): Board = {
		if (currentTile.isDefined) {
			place(pos, currentTile.get).copy(currentTile = Option.empty, currentPos = Option(pos))
		} else if (pos == currentPos.get) {
			this
		} else {
			val placed = place(pos, tiles(currentPos.get))
			placed.copy(tiles = placed.tiles.removed(currentPos.get), currentPos = Option(pos))
		}
	}

	@throws[PlacementException]("Current tile not placed")
	def pickupCurrentTile(): Board = {
		if (currentPos.isEmpty) {
			throw PlacementException("Current tile not placed")
		}
		copy(tiles = tiles.removed(currentPos.get), currentTile = Option(tiles(currentPos.get)), currentPos = Option.empty)
	}

	@throws[PlacementException]("Already occupied")
	def place(pos: Position, tile: Tile): Board = {
		if (tiles.contains(pos)) {
			throw PlacementException("Already occupied")
		}
		copy(tiles.updated(pos, tile))
	}

	@throws[PlacementException]("Tile not placed")
	def commit(): Board = {
		if (currentPos.isEmpty) {
			throw PlacementException("Tile not placed")
		}
		copy(currentPos = Option.empty, currentTile = Option(TileBuilder.randomTile()))
	}

	def boardToString: String = {
		if (tiles.isEmpty) {
			return "empty"
		}
		val mapWidth = 50
		val mapHeight = 25
		val tileHeight = 5
		val tileWidth = 12
		val border = 2
		val margin = 2

		val off = tiles.keySet.reduce((a, b) => Position(a.x min b.x, a.y min b.y))
		boardToString(off, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame = false)
	}

	@throws[IllegalArgumentException]
	def boardToString(offset: Position, mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int, frame: Boolean = true, highlight: Option[Position] = Option.empty): String = {
		require(mapWidth > 0)
		require(mapHeight > 0)
		if (highlight.isDefined) {
			require(margin > 0)
		}

		val rows = Math.ceil(mapHeight / (tileHeight + margin / 2).doubleValue + offset.y).intValue
		val cols = Math.ceil(mapWidth / (tileWidth + margin).doubleValue + offset.x).intValue

		val lines = for (y <- offset.y until rows; line <- 0 until tileHeight + margin / 2) yield
			(for (x <- offset.x until cols) yield if (tiles.contains(Position(x, y))) {
				if (line < tileHeight && highlight.isDefined && highlight.get == Position(x + 1, y)) {
					this.tiles(Position(x, y)).printLine(line, tileWidth, tileHeight, border, 0) + " " * (margin - 1) + ">"
				} else if (line < tileHeight && highlight.isDefined && highlight.get == Position(x, y)) {
					this.tiles(Position(x, y)).printLine(line, tileWidth, tileHeight, border, 0) + "<" + " " * (margin - 1)
				} else {
					this.tiles(Position(x, y)).printLine(line, tileWidth, tileHeight, border, margin)
				}
			} else {
				if (line < tileHeight && highlight.isDefined && highlight.get == Position(x + 1, y)) {
					" " * tileWidth + " " * (margin - 1) + ">"
				} else if (line < tileHeight && highlight.isDefined && highlight.get == Position(x, y)) {
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
