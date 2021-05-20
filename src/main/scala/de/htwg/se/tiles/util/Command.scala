package de.htwg.se.tiles.util

import scala.util.Try

trait Command {
	def execute(): Try[_]

	def undo(): Try[_]

	def redo(): Try[_] = execute()
}
