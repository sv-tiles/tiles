package de.htwg.se.tiles.control

import de.htwg.se.tiles.model.{Board, Tile}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.HashMap

class ControllerSpec extends AnyWordSpec with Matchers {
	"A controller" should {
		val board = Board(new HashMap()
			.updated((0, 0), Tile.random())
			.updated((1, 0), Tile.random())
			.updated((0, 1), Tile.random())
			.updated((1, 1), Tile.random())
		)
		"clear board" in {
			val controller = new Controller(board)
			controller.clear()
			controller.board shouldBe Board().copy(currentTile = controller.board.currentTile)
		}
		"place tiles" in {
			val controller = new Controller(board)
			controller.placeTile((10, 10))
			controller.board shouldBe board.placeCurrentTile((10, 10))
		}
		"rotate current tile" in {
			val controller = new Controller(board)
			controller.rotate(true)
			controller.board shouldBe board.rotateCurrentTile(true)
		}
		"commit placed tile" in {
			val controller = new Controller(board)
			val board2 = board.placeCurrentTile((10, 10))
			controller.placeTile((10, 10))
			controller.commit()
			controller.board shouldBe board2.commit().copy(currentTile = controller.board.currentTile)
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

			controller.mapToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame) shouldBe board.boardToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame)
		}
		"print current tile as string" in {
			val controller = new Controller(board)
			val width = 15
			val height = 5
			val margin = 2
			val border = 2
			controller.currentTileToString(width, height, border, margin) shouldBe board.currentTile.get.tileToString(width, height, border, margin)
		}
	}
}