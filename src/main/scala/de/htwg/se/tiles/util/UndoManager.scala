package de.htwg.se.tiles.util

import scala.util.Try

class UndoManager {
	private var undoStack: List[Command] = Nil
	private var redoStack: List[Command] = Nil

	def execute(command: Command): Try[_] = {
		undoStack = command :: undoStack
		redoStack = Nil
		command.execute()
	}

	def redo(): Try[_] = {
		redoStack match {
			case Nil => Try()
			case head :: stack =>
				val t = head.redo()
				redoStack = stack
				undoStack = head :: undoStack
				t
		}
	}

	def undo(): Try[_] = {
		undoStack match {
			case Nil => Try()
			case head :: stack =>
				val t = head.undo()
				undoStack = stack
				redoStack = head :: redoStack
				t
		}
	}
}
