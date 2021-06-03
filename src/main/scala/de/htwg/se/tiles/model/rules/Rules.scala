package de.htwg.se.tiles.model.rules

import de.htwg.se.tiles.model.{Board, Position, Tile}

import scala.collection.immutable.HashMap
import scala.util.{Failure, Try}

trait Rules {
	def canPlace(tile: Tile, tiles: HashMap[Position, Tile], at: Position): Boolean

	// @throws[IllegalArgumentException]("No tile to place")
	def canPlace(b: Board): Try[Boolean] =
		b.currentPos.fold[Try[Boolean]](Failure(new IllegalArgumentException("No tile to place")))(
			p => b.pickupCurrentTile().map(b2 => canPlace(b.tiles(p), b2.tiles, p))
		)

	def randomPlaceable(b: Board): Tile
}
