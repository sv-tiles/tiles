package de.htwg.se.tiles.control.controllerComponent.controllerBaseImpl

import de.htwg.se.tiles.model.Position
import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.util.Command

import scala.util.{Failure, Success, Try}

class PlaceTileCommand(controller: Controller, pos: Position) extends Command {
	private var board: BoardInterface = controller.board

	override def execute(): Try[_] = Try(this.board = controller.board).flatMap(_ => controller.board.placeCurrentTile(pos).map(b => controller.board = b))
		.recoverWith(e => {
			if (controller.board.currentPos.isDefined) {
				controller.board.pickupCurrentTile().map(b => {
					controller.board = b
					board = b
				})
			}
			Failure(e)
		})

	override def undo(): Try[_] = Success(controller.board = board)
}
