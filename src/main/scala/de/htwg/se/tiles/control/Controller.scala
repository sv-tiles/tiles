package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.Board
import de.htwg.se.tiles.util.Observable

import scala.util.Try

class Controller(var board: Board = Board()) extends Observable[(Boolean, String)] {

	def clear(): Unit = {
		board = Board()
		notifyObservers((true, ""))
	}

	def placeTile(pos: (Int, Int)): Unit =
		Try(board.placeCurrentTile(pos)).map(b => board = b).fold(e => (false, e.getMessage), _ => notifyObservers((true, "")))

	def rotate(clockwise: Boolean): Unit = {
		board = board.rotateCurrentTile(clockwise)
		notifyObservers((true, ""))
	}

	def commit(): Unit =
		Try(board.commit()).map(b => board = b).fold(e => (false, e.getMessage), _ => notifyObservers((true, "")))

	def mapToString(offset: (Int, Int), mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int, frame: Boolean = true, highlight: Option[(Int, Int)] = Option.empty): String = {
		board.boardToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame, highlight)
	}

	def currentTileToString(width: Int, height: Int, border: Int, margin: Int): String =
		board.currentTile.map[String](t => t.tileToString(width, height, border, margin)).getOrElse("Tile placed")
}
