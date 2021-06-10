package de.htwg.se.tiles.control.controllerComponent

import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.model.{GameSnapshot, Position}
import de.htwg.se.tiles.util.Observable

import scala.util.Try

trait ControllerInterface extends Observable[(Boolean, String)] {
	def board: BoardInterface

	def clear(): Unit

	def placeTile(pos: (Int, Int)): Unit

	def placeTile(pos: Position): Unit

	def pickUpTile(): Unit

	def rotate(clockwise: Boolean): Unit

	def commit(): Unit

	def mapToString(offset: (Int, Int), mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int, frame: Boolean = true, highlight: Option[(Int, Int)] = Option.empty): String

	def currentTileToString(width: Int, height: Int, border: Int, margin: Int): Try[String]

	def toSnapshot: GameSnapshot

	def restoreSnapshot(snapshot: GameSnapshot): ControllerInterface

	def undo(): Unit

	def redo(): Unit
}
