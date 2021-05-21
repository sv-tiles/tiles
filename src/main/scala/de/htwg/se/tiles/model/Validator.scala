package de.htwg.se.tiles.model

import scala.collection.immutable.HashMap
import scala.util.Try

trait Validator {
	def canPlace(tile: Tile, tiles: HashMap[Position, Tile], at: Position): Boolean

	// @throws[IllegalArgumentException]("No tile to place")
	def canPlace(b: Board): Try[Boolean] =
		b.currentPos.fold[Try[Boolean]](Try(throw new IllegalArgumentException("No tile to place")))(
			p => b.pickupCurrentTile().map(b2 => canPlace(b.tiles(p), b2.tiles, p))
		)

	def randomPlaceable(b: Board): Tile
}
