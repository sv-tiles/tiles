package de.htwg.se.tiles.model

import scala.collection.immutable.HashMap

trait Validator {
	def canPlace(tile: Tile, tiles: HashMap[Position, Tile], at: Position): Boolean

	@throws[IllegalArgumentException]("No tile to place")
	def canPlace(b: Board): Boolean = {
		if (b.currentPos.isEmpty) {
			throw new IllegalArgumentException("No tile to place")
		}
		canPlace(b.tiles(b.currentPos.get), b.pickupCurrentTile().tiles, b.currentPos.get)
	}

	def randomPlaceable(b: Board): Tile
}
