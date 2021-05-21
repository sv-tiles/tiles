package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.{Board, Position}
import de.htwg.se.tiles.util.Command

import scala.util.{Success, Try}

class PlaceTileCommand(controller: Controller, pos: (Int, Int)) extends Command {
	private var board: Board = controller.board

	override def execute(): Try[_] = Try(this.board = controller.board).flatMap(_ => controller.board.placeCurrentTile(Position(pos._1, pos._2)).map(b => controller.board = b))
		.recoverWith(e => {
			if (controller.board.currentPos.isDefined) {
				controller.board.pickupCurrentTile().map(b => {
					controller.board = b
					board = b
				})
			}
			Try(throw e)
		})

	override def undo(): Try[_] = Success(controller.board = board)
}
