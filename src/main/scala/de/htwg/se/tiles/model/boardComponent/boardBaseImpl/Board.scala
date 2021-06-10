package de.htwg.se.tiles.model.boardComponent.boardBaseImpl

import de.htwg.se.tiles.model.Position
import de.htwg.se.tiles.model.boardComponent.{BoardInterface, TileBuilderInterface, TileInterface}
import de.htwg.se.tiles.model.rulesComponent.RulesInterface

import scala.collection.immutable.HashMap
import scala.util.{Failure, Success, Try}


// @throws[IllegalArgumentException]
case class Board(tiles: HashMap[Position, Tile] = new HashMap[Position, Tile](), currentTile: Option[TileInterface] = Option(TileBuilder.randomTile()), currentPos: Option[Position] = Option.empty) extends BoardInterface {
	require(currentTile.isEmpty ^ currentPos.isEmpty, "current tile XOR current pos! (" + currentTile.isDefined + ", " + currentPos.isDefined + ")")
	require(currentPos.isEmpty || tiles.contains(currentPos.get), "At current pos has to be a tile")

	override def clear: BoardInterface = Board()

	override def getTileBuilder: TileBuilderInterface = TileBuilder

	override def rotateCurrentTile(clockwise: Boolean = true): Board = if (currentTile.isDefined)
		copy(currentTile = currentTile.map(t => t.rotate(clockwise))) else
		copy(tiles = tiles.updated(currentPos.get, tiles(currentPos.get).rotate(clockwise)))

	override def placeCurrentTile(pos: Position): Try[Board] = {
		if (currentTile.isDefined) {
			place(pos, currentTile.get).map(b => b.copy(currentTile = Option.empty, currentPos = Option(pos)))
		} else if (pos == currentPos.get) {
			Success(this)
		} else {
			place(pos, tiles(currentPos.get)).map(placed => placed.copy(tiles = placed.tiles.removed(currentPos.get), currentPos = Option(pos)))
		}
	}

	override def pickupCurrentTile(): Try[Board] = Try {
		if (currentPos.isEmpty) {
			return Failure(PlacementException("Current tile not placed"))
		}
		copy(tiles = tiles.removed(currentPos.get), currentTile = Option(tiles(currentPos.get)), currentPos = Option.empty)
	}

	override def place(pos: Position, tile: TileInterface): Try[Board] = Try {
		if (tiles.contains(pos)) {
			return Failure(PlacementException("Already occupied"))
		}
		copy(tiles = tiles.updated(pos, Tile(tile.north, tile.east, tile.south, tile.west, tile.center)))
	}

	override def commit(rules: RulesInterface): Try[Board] = rules.canPlace(this).flatMap(canPlace => Try {
		if (!canPlace) {
			return Failure(PlacementException("Placement not valid"))
		}
		copy(currentPos = Option.empty, currentTile = Option(rules.randomPlaceable(this)))
	})

	override def boardToString: String = {
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
		boardToString(off, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame = false).get
	}

	override def boardToString(offset: Position, mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int, frame: Boolean = true, highlight: Option[Position] = Option.empty): Try[String] = Try {
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
					this.tiles(Position(x, y)).printLine(line, tileWidth, tileHeight, border, 0).get + " " * (margin - 1) + ">"
				} else if (line < tileHeight && highlight.isDefined && highlight.get == Position(x, y)) {
					this.tiles(Position(x, y)).printLine(line, tileWidth, tileHeight, border, 0).get + "<" + " " * (margin - 1)
				} else {
					this.tiles(Position(x, y)).printLine(line, tileWidth, tileHeight, border, margin).get
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
