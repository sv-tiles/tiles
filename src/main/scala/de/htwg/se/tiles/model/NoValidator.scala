package de.htwg.se.tiles.model

import scala.collection.immutable.HashMap

case class NoValidator() extends Validator {
	override def canPlace(tile: Tile, tiles: HashMap[Position, Tile], at: Position): Boolean = !tiles.contains(at)

	override def randomPlaceable(b: Board): Tile = TileBuilder.randomTile()
}
