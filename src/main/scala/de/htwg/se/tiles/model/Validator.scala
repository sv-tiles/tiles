package de.htwg.se.tiles.model

import scala.collection.immutable.HashMap

trait Validator {
	def canPlace(tile: Tile, tiles: HashMap[Position, Tile], at: Position): Boolean

	@throws[IllegalArgumentException]("No tile to place")
	def canPlace(b: Board): Boolean

	def randomPlaceable(b: Board): Tile
}
