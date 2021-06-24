package de.htwg.se.tiles.util

import de.htwg.se.tiles.control.controllerComponent.controllerBaseImpl.{Controller, PlaceTileCommand}
import de.htwg.se.tiles.model.Position
import de.htwg.se.tiles.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.tiles.model.fileIoComponent.fileIoFake.FileIoFake
import de.htwg.se.tiles.model.playerComponent.playerBaseImpl.PlayerBase
import de.htwg.se.tiles.model.rulesComponent.rulesFakeImpl.RulesFake
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scalafx.scene.paint.Color


class UndoManagerSpec extends AnyWordSpec with Matchers {
	"An UndoManger" should {
		val manager: UndoManager = new UndoManager()
		val controller: Controller = new Controller(Board(), RulesFake(), undoManager = manager, playerFactory = (name: String, color: Color) => PlayerBase(name, color), fileIo = new FileIoFake())

		"undo redo" in {
			val board = controller.board
			manager.execute(new PlaceTileCommand(controller, Position(0, 0)))
			val board2 = controller.board

			board should not be board2

			manager.undo()

			controller.board shouldBe board

			manager.undo()

			controller.board shouldBe board

			manager.redo()

			controller.board shouldBe board2

			manager.redo()

			controller.board shouldBe board2
		}
	}

}
