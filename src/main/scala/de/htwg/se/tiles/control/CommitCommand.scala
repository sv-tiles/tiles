package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.{Board, Validator}
import de.htwg.se.tiles.util.Command

import scala.util.{Success, Try}

class CommitCommand(controller: Controller, validator: Validator) extends Command {
	private var board: Board = controller.board;

	override def execute(): Try[_] = Try(this.board = controller.board, controller.board = controller.board.commit(validator))

	override def undo(): Try[_] = Success(controller.board = board)
}
