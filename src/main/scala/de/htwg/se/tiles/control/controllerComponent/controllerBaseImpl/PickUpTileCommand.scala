package de.htwg.se.tiles.control.controllerComponent.controllerBaseImpl

import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.util.Command

import scala.util.{Success, Try}

class PickUpTileCommand(controller: Controller) extends Command {
	private var board: BoardInterface = controller.board

	override def execute(): Try[_] = Try(this.board = controller.board).flatMap(_ => controller.board.pickupCurrentTile()
		.map(b => controller.board = b))

	override def undo(): Try[_] = Success(controller.board = board)
}
