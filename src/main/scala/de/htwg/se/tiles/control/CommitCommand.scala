package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.Board
import de.htwg.se.tiles.model.rules.Rules
import de.htwg.se.tiles.util.Command

import scala.util.{Failure, Success, Try}

class CommitCommand(controller: Controller, rules: Rules) extends Command {
	private var board: Board = controller.board;
	private var newBoard: Board = controller.board
	private var error = Option.empty[Throwable]

	override def execute(): Try[_] = Try(this.board = controller.board).flatMap(_ => controller.board.commit(rules).map(b => {
		controller.board = b
		newBoard = b
	})).recoverWith(e => {
		error = Option(e)
		Failure(e)
	})

	override def redo(): Try[_] =
		error.fold[Try[_]]({
			controller.board = newBoard
			Success()
		})(e => {
			controller.board = board
			Failure(e)
		})

	override def undo(): Try[_] = Success(controller.board = board)
}
