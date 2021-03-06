package de.htwg.se.tiles.control.controllerComponent.controllerBaseImpl

import de.htwg.se.tiles.model.Direction
import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.model.rulesComponent.RulesInterface
import de.htwg.se.tiles.util.Command

import scala.util.{Failure, Success, Try}

class CommitCommand(controller: Controller, rules: RulesInterface, placePeople: Option[Direction] = Option.empty) extends Command {
	private var board: BoardInterface = controller.board;
	private var newBoard: BoardInterface = controller.board
	private var error = Option.empty[Throwable]

	override def execute(): Try[_] = Try(this.board = controller.board).flatMap(_ => controller.board.commit(rules, placePeople).map(b => {
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
