package de.htwg.se.tiles.model.rulesComponent

import de.htwg.se.tiles.model.boardComponent.{BoardInterface, Island, TileInterface}
import de.htwg.se.tiles.model.{Direction, Position, SubPosition}

import scala.collection.immutable.HashMap
import scala.util.{Failure, Try}

trait RulesInterface {
	def maxPeople: Int

	def canPlace(tile: TileInterface, tiles: HashMap[Position, TileInterface], at: Position): Boolean

	def canPlace(b: BoardInterface, people: Option[Direction]): Try[Boolean] =
		b.currentPos.fold[Try[Boolean]](Failure(new IllegalArgumentException("No tile to place")))(
			p => b.pickupCurrentTile().map(b2 => canPlace(b.tiles(p), b2.tiles, p) &&
				people.fold(true)(p => findIsland(b, SubPosition(b.currentPos.get, p)).owners.isEmpty)
			)
		)

	def randomPlaceable(b: BoardInterface): TileInterface

	def assignPoints(b: BoardInterface): BoardInterface

	def findIsland(b: BoardInterface, start: SubPosition): Island
}
