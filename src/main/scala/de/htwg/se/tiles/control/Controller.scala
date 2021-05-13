package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.{Board, Position}
import de.htwg.se.tiles.util.Observable

import scala.util.Try

class Controller(var board: Board = Board()) extends Observable[(Boolean, String)] {

	def clear(): Unit = {
		board = Board()
		notifyObservers((true, ""))
	}

	def placeTile(pos: (Int, Int)): Unit =
		Try(board.placeCurrentTile(Position(pos._1, pos._2))).map(b => board = b).fold(e => {
			if (board.currentPos.isDefined) {
				board = board.pickupCurrentTile()
			}
			notifyObservers((false, e.getMessage))
		}, _ => notifyObservers((true, "")))

	def rotate(clockwise: Boolean): Unit = {
		board = board.rotateCurrentTile(clockwise)
		notifyObservers((true, ""))
	}

	def commit(): Unit =
		Try(board.commit()).map(b => board = b).fold(e => notifyObservers((false, e.getMessage)), _ => notifyObservers((true, "")))

	def mapToString(offset: (Int, Int), mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int, frame: Boolean = true, highlight: Option[(Int, Int)] = Option.empty): String = {
		board.boardToString(Position(offset._1, offset._2), mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame, highlight.map(pos => Position(pos._1, pos._2)))
	}

	def currentTileToString(width: Int, height: Int, border: Int, margin: Int): String =
		board.currentTile.map[String](t => t.tileToString(width, height, border, margin)).getOrElse("Tile placed")
}
