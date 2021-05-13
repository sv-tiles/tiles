package de.htwg.se.tiles.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.HashMap


class BoardSpec extends AnyWordSpec with Matchers {
	"A Board" when {
		"initialized without tiles" should {
			val board = Board()
			"be empty" in {
				board.tiles.isEmpty shouldBe true
			}
			"return empty from boardToString" in {
				board.boardToString shouldBe "empty"
			}
		}
		"initialized" should {
			"throw if current tile and pos are used" in {
				an[IllegalArgumentException] should be thrownBy Board(new HashMap().updated(Position(0, 0), TileBuilder.randomTile()), Option(TileBuilder.randomTile()), Option(Position(0, 0)))
			}
			"throw if current pos is not occupied" in {
				an[IllegalArgumentException] should be thrownBy Board(new HashMap(), Option.empty, Option(Position(0, 0)))
			}
		}
		"printed" should {
			val offset = Position(0, 0)
			val mapWidth = 50
			val mapHeight = 20
			val tileWidth = 10
			val tileHeight = 5
			val border = 2
			val margin = 2
			"return blank if empty" in {
				Board().boardToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame = false) shouldBe (" " * mapWidth + "\n") * mapHeight
			}
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val board = Board().place(Position(0, 0), tile).place(Position(1, 0), tile)
			"not throw" in {
				noException should be thrownBy board.boardToString
			}
			"throw if width <=0" in {
				an[IllegalArgumentException] should be thrownBy board.boardToString(offset, 0, mapHeight, tileWidth, tileHeight, border, margin)
			}
			"throw if height <=0" in {
				an[IllegalArgumentException] should be thrownBy board.boardToString(offset, mapWidth, 0, tileWidth, tileHeight, border, margin)
			}
			"have the right size" in {
				val lines = board.boardToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame = false).split("\n")
				lines.map(l => l.length).sum shouldBe mapWidth * mapHeight
				lines.head.length shouldBe mapWidth
				lines.length shouldBe mapHeight

			}
			"have the right size with frame" in {
				val lines = board.boardToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame = true).split("\n")
				lines.map(l => l.length).sum shouldBe (mapWidth + 2) * (mapHeight + 2)
				lines.head.length shouldBe (mapWidth + 2)
				lines.length shouldBe (mapHeight + 2)

			}
			"be nearly the same with and without highlight" in {
				board.boardToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame = true) shouldBe board.boardToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame = true, Option(Position(0, 0))).replaceAll("[<>]", " ")
				board.boardToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame = false) shouldBe board.boardToString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame = false, Option(Position(0, 0))).replaceAll("[<>]", " ")
			}
			"have correct highlighting" in {
				board.boardToString(Position(0, 0), 6, 6, 5, 6, 2, 1, false, Option(Position(0, 0))) shouldBe "  _  <\n_____<\n_____<\n_____<\n_____<\n  _  <\n"
				board.boardToString(Position(0, 0), 6, 6, 5, 6, 2, 1, false, Option(Position(1, 0))) shouldBe "  _  >\n_____>\n_____>\n_____>\n_____>\n  _  >\n"
			}
		}
		"tile added" should {
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val board = Board().place(Position(0, 0), tile)
			"throw if already occupied" in {
				an[PlacementException] should be thrownBy board.place(Position(0, 0), tile)
			}
			"add tile" in {
				val mapNew = board.place(Position(1, 0), tile)
				mapNew.tiles.size shouldBe board.tiles.size + 1
			}
		}
		"place current tile" should {
			val tile1 = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val tile2 = Tile(Terrain.Hills, Terrain.Hills, Terrain.Hills, Terrain.Hills, Terrain.Hills)
			"do nothing if already in place" in {
				val board = Board(new HashMap().updated(Position(10, 10), tile1), Option.empty, Option(Position(10, 10)))
				board.placeCurrentTile(Position(10, 10)) shouldBe board
			}
			"throw exception if already occupied" in {
				val board1 = Board(new HashMap().updated(Position(10, 10), tile1).updated(Position(0, 0), tile2), Option.empty, Option(Position(0, 0)))
				an[PlacementException] should be thrownBy board1.placeCurrentTile(Position(10, 10))

				val board2 = Board(new HashMap().updated(Position(10, 10), tile1), Option(tile2), Option.empty)
				an[PlacementException] should be thrownBy board2.placeCurrentTile(Position(10, 10))
			}
			"place it" in {
				val board1 = Board(new HashMap().updated(Position(0, 0), tile1), Option.empty, Option(Position(0, 0)))
				board1.placeCurrentTile(Position(10, 10)) shouldBe board1.copy(tiles = new HashMap().updated(Position(10, 10), tile1), currentPos = Option(Position(10, 10)))

				val board2 = Board(new HashMap(), Option(tile1), Option.empty)
				board2.placeCurrentTile(Position(10, 10)) shouldBe board2.copy(tiles = board2.tiles.updated(Position(10, 10), tile1), currentPos = Option(Position(10, 10)), currentTile = Option.empty)

				val board3 = Board(new HashMap().updated(Position(0, 0), tile1), Option(tile2), Option.empty)
				board3.placeCurrentTile(Position(10, 10)) shouldBe board3.copy(tiles = board3.tiles.updated(Position(10, 10), tile2), currentPos = Option(Position(10, 10)), currentTile = Option.empty)
			}
		}
		"pickup current tile" should {
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val board1 = Board(new HashMap().updated(Position(0, 0), tile), Option.empty, Option(Position(0, 0)))
			val board2 = Board(new HashMap(), Option(tile), Option.empty)
			"throw if current tile not placed" in {
				an[PlacementException] should be thrownBy board2.pickupCurrentTile()
			}
			"work" in {
				board1.pickupCurrentTile() shouldBe board1.copy(tiles = board1.tiles.removed(Position(0, 0)), currentPos = Option.empty, currentTile = Option(tile))
			}
		}
		"rotate current tile" should {
			val tile1 = Tile(Terrain.Hills, Terrain.Plains, Terrain.Mountains, Terrain.Water, Terrain.Forest)
			"rotate the tile" in {
				val board1 = Board(new HashMap().updated(Position(0, 0), tile1), Option.empty, Option(Position(0, 0)))
				board1.rotateCurrentTile(true) shouldBe board1.copy(tiles = new HashMap().updated(Position(0, 0), tile1.rotate(true)))
				board1.rotateCurrentTile(false) shouldBe board1.copy(tiles = new HashMap().updated(Position(0, 0), tile1.rotate(false)))

				val board2 = Board(new HashMap(), Option(tile1), Option.empty)
				board2.rotateCurrentTile(true) shouldBe board2.copy(currentTile = Option(tile1.rotate(true)))
				board2.rotateCurrentTile(false) shouldBe board2.copy(currentTile = Option(tile1.rotate(false)))
			}
		}
		"commit" should {
			val tile1 = Tile(Terrain.Hills, Terrain.Plains, Terrain.Mountains, Terrain.Water, Terrain.Forest)
			"throw exception if tile not placed" in {
				val board = Board(new HashMap(), Option(tile1), Option.empty)
				an[PlacementException] should be thrownBy board.commit()
			}
			"generate a new current tile and discard current pos" in {
				val board = Board(new HashMap().updated(Position(0, 0), tile1), Option.empty, Option(Position(0, 0)))
				val committed = board.commit()
				committed shouldBe board.copy(currentPos = Option.empty, currentTile = committed.currentTile)
				committed.currentTile should not be Option.empty
			}
		}
	}
}
