package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.Board
import de.htwg.se.tiles.model.rules.Rules
import de.htwg.se.tiles.util.Command

import scala.util.{Success, Try}

class CommitCommand(controller: Controller, rules: Rules) extends Command {
	private var board: Board = controller.board;

	override def execute(): Try[_] = Try(this.board = controller.board).flatMap(_ => controller.board.commit(rules).map(b => controller.board = b))

	override def undo(): Try[_] = Success(controller.board = board)
}
