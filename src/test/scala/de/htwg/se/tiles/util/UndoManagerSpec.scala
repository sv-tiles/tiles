package de.htwg.se.tiles.util

import de.htwg.se.tiles.control.{Controller, PlaceTileCommand}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec


class UndoManagerSpec extends AnyWordSpec with Matchers {
	"A UndoManger" should {
		val manager: UndoManager = new UndoManager()
		val controller: Controller = new Controller(undoManager = manager)

		"undo redo" in {
			val board = controller.board
			manager.execute(new PlaceTileCommand(controller, (0, 0)))
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
