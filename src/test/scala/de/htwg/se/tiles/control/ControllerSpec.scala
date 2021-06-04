package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.{Board, Position, TileBuilder}
import de.htwg.se.tiles.util.Command
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.HashMap
import scala.util.{Failure, Try}

class ControllerSpec extends AnyWordSpec with Matchers {
	"A controller" should {
		val board = Board(new HashMap()
			.updated(Position(0, 0), TileBuilder.randomTile())
			.updated(Position(1, 0), TileBuilder.randomTile())
			.updated(Position(0, 1), TileBuilder.randomTile())
			.updated(Position(1, 1), TileBuilder.randomTile())
		)
		"clear board" in {
			val controller = new Controller(board)
			controller.clear()
			controller.board shouldBe Board().copy(currentTile = controller.board.currentTile)
		}
		"place tiles" in {
			val controller = new Controller(board)
			controller.placeTile((10, 10))
			controller.board shouldBe board.placeCurrentTile(Position(10, 10)).get

			controller.placeTile((1, 1))
			controller.placeTile((1, 1))
		}
		"pick tile up" in {
			val board2 = board.placeCurrentTile(Position(-9, -9)).get
			val controller = new Controller(board2)
			controller.pickUpTile()
			controller.board shouldBe board2.pickupCurrentTile().get

			controller.pickUpTile()
		}
		"rotate current tile" in {
			val controller = new Controller(board)
			controller.rotate(true)
			controller.board shouldBe board.rotateCurrentTile(true)
		}
		"commit placed tile" in {
			val controller = new Controller(board)
			val board2 = board.placeCurrentTile(Position(10, 10)).get
			controller.placeTile((10, 10))
			controller.commit()
			controller.board shouldBe board2.commit(controller.rules).get.copy(currentTile = controller.board.currentTile)
		}
		"print map as string" in {
			val controller = new Controller(board)
			val offset = (0, 0)
			val mapWidth = 120
			val mapHeight = 60
			val tileWidth = 15
			val tileHeight = 5
			val border = 2
			val margin = 2
			val frame = true

			controller.mapToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame) shouldBe board.boardToString(Position(offset._1, offset._2), mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame).get
		}
		"fail to print map as string if invalid" in {
			val controller = new Controller(board)
			val offset = (0, 0)
			val mapWidth = 0
			val mapHeight = 0
			val tileWidth = 0
			val tileHeight = 5
			val border = 2
			val margin = 2
			val frame = true

			controller.mapToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame) shouldBe "requirement failed"
		}
		"print current tile as string" in {
			val controller = new Controller(board)
			val width = 15
			val height = 5
			val margin = 2
			val border = 2
			controller.currentTileToString(width, height, border, margin) shouldBe board.currentTile.get.tileToString(width, height, border, margin)
		}
		"store and restore snapshots" in {
			val controller = new Controller(board)
			val snapshot = controller.toSnapshot

			controller.board.currentPos.isEmpty shouldBe true

			controller.placeTile((1, 10))

			controller.board.currentPos.isDefined shouldBe true

			controller.restoreSnapshot(snapshot).board.currentPos.isEmpty shouldBe true
		}
		"correctly undo redo" in {
			val controller = new Controller()

			val board = controller.board

			controller.placeTile((0, 0))

			val board2 = controller.board

			board should not be board2

			controller.undo()

			controller.board shouldBe board

			controller.redo()

			controller.board shouldBe board2

			controller.pickUpTile()

			controller.undo()

			controller.redo()

			controller.undo()

			controller.commit()

			controller.undo()

			controller.redo()

			controller.clear()

			controller.undo()

			controller.redo()

			controller.rotate(true)

			controller.undo()

			controller.redo()

			controller.undoManager.execute(new TestErrorUndoCommand())

			controller.undo()

			controller.redo()

		}
	}
}

class TestErrorUndoCommand extends Command {
	override def execute(): Try[_] = Failure(new RuntimeException("Test"))

	override def undo(): Try[_] = Failure(new RuntimeException("Test"))
}
