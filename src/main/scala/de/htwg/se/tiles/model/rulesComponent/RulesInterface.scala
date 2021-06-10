package de.htwg.se.tiles.model.rulesComponent

import de.htwg.se.tiles.model.Position
import de.htwg.se.tiles.model.boardComponent.{BoardInterface, TileInterface}

import scala.collection.immutable.HashMap
import scala.util.{Failure, Try}

trait RulesInterface {
	def canPlace(tile: TileInterface, tiles: HashMap[Position, TileInterface], at: Position): Boolean

	def canPlace(b: BoardInterface): Try[Boolean] =
		b.currentPos.fold[Try[Boolean]](Failure(new IllegalArgumentException("No tile to place")))(
			p => b.pickupCurrentTile().map(b2 => canPlace(b.tiles(p), b2.tiles, p))
		)

	def randomPlaceable(b: BoardInterface): TileInterface
}
