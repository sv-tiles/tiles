package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.{Board, Position}
import de.htwg.se.tiles.util.Command

import scala.util.{Failure, Success, Try}

class PlaceTileCommand(controller: Controller, pos: (Int, Int)) extends Command {
	private var board: Board = controller.board

	override def execute(): Try[_] = Try(this.board = controller.board, controller.board = controller.board.placeCurrentTile(Position(pos._1, pos._2)))
		.recoverWith(e => {
			if (controller.board.currentPos.isDefined) {
				controller.board = controller.board.pickupCurrentTile()
				board = controller.board
			}
			Failure(e)
		})

	override def undo(): Try[_] = Success(controller.board = board)
}
