package de.htwg.se.tiles.model.boardComponent

import de.htwg.se.tiles.model.playerComponent.PlayerInterface
import de.htwg.se.tiles.model.rulesComponent.RulesInterface
import de.htwg.se.tiles.model.{Direction, Position}

import scala.collection.immutable.HashMap
import scala.util.Try

trait BoardInterface {
	def currentTile: Option[TileInterface]

	def currentPos: Option[Position]

	def tiles: HashMap[Position, TileInterface]

	def getTileBuilder: TileBuilderInterface

	def players: Vector[PlayerInterface]

	def getCurrentPlayer: PlayerInterface

	def rotateCurrentTile(clockwise: Boolean = true): BoardInterface

	def placeCurrentTile(pos: Position): Try[BoardInterface]

	def pickupCurrentTile(): Try[BoardInterface]

	def place(pos: Position, tile: TileInterface): Try[BoardInterface]

	def commit(rules: RulesInterface, people: Option[Direction] = Option.empty): Try[BoardInterface]

	def boardToString: String

	def boardToString(offset: Position, mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int, frame: Boolean = true, highlight: Option[Position] = Option.empty): Try[String]

	def clear: BoardInterface

	def updatePlayers(players: Vector[PlayerInterface]): BoardInterface
}
