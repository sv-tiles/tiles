package de.htwg.se.tiles.model

import org.scalatest.PrivateMethodTester
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.HashMap

class BasicValidatorSpec extends AnyWordSpec with Matchers with PrivateMethodTester {
	"A BasicValidator" should {
		val validator = BasicValidator()
		val makeFit = PrivateMethod[Tile](Symbol("makeFit"))
		val possiblePositions = PrivateMethod[Set[Position]](Symbol("possiblePositions"))

		val pos = Position(0, 0)
		val board = Board(tiles = new HashMap[Position, Tile]()
			.updated(pos.north(), Tile(Terrain.Plains))
			.updated(pos.east(), Tile(Terrain.Water))
			.updated(pos.south(), Tile(Terrain.Forest))
			.updated(pos.west(), Tile(Terrain.Hills))
		)
		val tile = Tile(Terrain.Plains, Terrain.Water, Terrain.Forest, Terrain.Hills, Terrain.Mountains)

		"make tiles fit" in {
			validator invokePrivate makeFit(Tile(Terrain.Mountains), board, pos) shouldBe tile
			validator invokePrivate makeFit(Tile(Terrain.Mountains), Board(), pos) shouldBe Tile(Terrain.Mountains)
		}
		"find possible positions" in {
			val board = Board(tiles = new HashMap[Position, Tile]()
				.updated(Position(0, 0), Tile(Terrain.Plains))
				.updated(Position(1, 0), Tile(Terrain.Plains))
				.updated(Position(10, 10), Tile(Terrain.Plains))
			)

			validator invokePrivate possiblePositions(board) shouldBe Set(
				Position(0, 0).neighbours(), Position(1, 0).neighbours(), Position(10, 10).neighbours()
			).flatten.filter(p => !board.tiles.contains(p))
		}
		"decide if can place tile" in {
			validator.canPlace(tile, new HashMap[Position, Tile](), pos) shouldBe true
			validator.canPlace(tile, new HashMap[Position, Tile]().updated(Position(100, 100), Tile(Terrain.Plains)), pos) shouldBe false
			validator.canPlace(tile, new HashMap[Position, Tile]().updated(pos, Tile(Terrain.Plains)), pos) shouldBe false
			validator.canPlace(tile, board.tiles, pos) shouldBe true
			validator.canPlace(tile.copy(north = Terrain.Mountains), board.tiles, pos) shouldBe false
			validator.canPlace(tile.copy(east = Terrain.Mountains), board.tiles, pos) shouldBe false
			validator.canPlace(tile.copy(south = Terrain.Mountains), board.tiles, pos) shouldBe false
			validator.canPlace(tile.copy(west = Terrain.Mountains), board.tiles, pos) shouldBe false

			an[IllegalArgumentException] should be thrownBy validator.canPlace(board)
			validator.canPlace(board.copy(currentTile = Option(tile)).placeCurrentTile(pos)) shouldBe true
		}
		"return random placeable tiles" in {
			noException should be thrownBy validator.randomPlaceable(Board())
			noException should be thrownBy validator.randomPlaceable(board)
		}
	}
}
