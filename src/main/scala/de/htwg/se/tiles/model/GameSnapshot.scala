package de.htwg.se.tiles.model

import de.htwg.se.tiles.control.Controller


object GameSnapshot {
	def fromController(controller: Controller): GameSnapshot = new GameSnapshot(controller.board)
}

case class GameSnapshot private(board: Board) {
	def toController: Controller = new Controller(board = board)
}
