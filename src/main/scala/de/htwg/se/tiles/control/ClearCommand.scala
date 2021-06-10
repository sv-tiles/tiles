package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.util.Command

import scala.util.{Success, Try}

class ClearCommand(controller: Controller) extends Command {
	private var board: BoardInterface = controller.board

	override def execute(): Try[_] =
		Try(this.board = controller.board, controller.board = controller.board.clear)

	override def undo(): Try[_] =
		Success(controller.board = board)

}
