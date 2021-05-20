package de.htwg.se.tiles.control

import de.htwg.se.tiles.model._
import de.htwg.se.tiles.util.{Observable, UndoManager}


class Controller(var board: Board = Board(), var validator: Validator = NoValidator(), var undoManager: UndoManager = new UndoManager()) extends Observable[(Boolean, String)] {

	def clear(): Unit = {
		undoManager.execute(new ClearCommand(this))
		notifyObservers((true, ""))
	}

	def placeTile(pos: (Int, Int)): Unit =
		undoManager.execute(new PlaceTileCommand(this, pos)).fold(
			e => notifyObservers((false, e.getMessage)),
			_ => notifyObservers((true, ""))
		)

	def rotate(clockwise: Boolean): Unit = {
		undoManager.execute(new RotateCommand(this, clockwise))
		notifyObservers((true, ""))
	}

	def commit(): Unit =
		undoManager.execute(new CommitCommand(this, validator)).fold(
			e => notifyObservers((false, e.getMessage)),
			_ => notifyObservers((true, ""))
		)

	def mapToString(offset: (Int, Int), mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int, frame: Boolean = true, highlight: Option[(Int, Int)] = Option.empty): String = {
		board.boardToString(Position(offset._1, offset._2), mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame, highlight.map(pos => Position(pos._1, pos._2)))
	}

	def currentTileToString(width: Int, height: Int, border: Int, margin: Int): String =
		board.currentTile.map[String](t => t.tileToString(width, height, border, margin)).getOrElse("Tile placed")

	def toSnapshot: GameSnapshot = GameSnapshot(board)

	def restoreSnapshot(snapshot: GameSnapshot): Controller = {
		board = snapshot.board
		this
	}

	def undo(): Unit =
		undoManager.undo().fold(e => notifyObservers((false, e.getMessage)), _ => notifyObservers((true, "")))

	def redo(): Unit =
		undoManager.redo().fold(e => notifyObservers((false, e.getMessage)), _ => notifyObservers((true, "")))
}
