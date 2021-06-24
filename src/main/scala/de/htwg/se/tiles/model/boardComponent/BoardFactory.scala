package de.htwg.se.tiles.model.boardComponent

import de.htwg.se.tiles.model.Position
import de.htwg.se.tiles.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.tiles.model.playerComponent.PlayerInterface

import scala.collection.immutable.HashMap

trait BoardFactory {
	def create(players: Vector[PlayerInterface], currentPlayer: Int, tiles: HashMap[Position, TileInterface], currentTile: Option[TileInterface], currentPos: Option[Position]): BoardInterface
	= Board(players, currentPlayer, tiles, currentTile, currentPos)
	// TODO fix
}
