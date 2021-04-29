package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.Board
import de.htwg.se.tiles.util.Observable

import scala.util.Try

class Controller(var board: Board = Board()) extends Observable[Unit] {

	def clear(): Unit =
		board = Board()

	def placeTile(pos: (Int, Int)): (Boolean, String) =
		Try(board.placeCurrentTile(pos)).map(b => board = b).fold(e => (false, e.getMessage), _ => (true, ""))

	def rotate(clockwise: Boolean): Unit =
		board = board.rotateCurrentTile(clockwise)

	def commit(): (Boolean, String) =
		Try(board.commit()).map(b => board = b).fold(e => (false, e.getMessage), _ => (true, ""))

	def mapToString(offset: (Int, Int), mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int, frame: Boolean = true, highlight: Option[(Int, Int)] = Option.empty): String =
		board.boardToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame, highlight)

	def currentTileToString(width: Int, height: Int, border: Int, margin: Int): String =
		board.currentTile.map[String](t => t.tileToString(width, height, border, margin)).orElse("Tile placed")
}
