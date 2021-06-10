package de.htwg.se.tiles.model.rulesComponent.rulesBaseImpl

import de.htwg.se.tiles.model.Position
import de.htwg.se.tiles.model.boardComponent.boardBaseImpl.{Board, Tile}
import de.htwg.se.tiles.model.boardComponent.{Terrain, boardBaseImpl}
import org.scalatest.PrivateMethodTester
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.HashMap

class BasicRulesSpec extends AnyWordSpec with Matchers with PrivateMethodTester {
	"BasicRules" should {
		val rules = RulesBase()
		val makeFit = PrivateMethod[Tile](Symbol("makeFit"))
		val possiblePositions = PrivateMethod[Set[Position]](Symbol("possiblePositions"))

		val pos = Position(0, 0)
		val board = Board(tiles = new HashMap[Position, Tile]()
			.updated(pos.north(), Tile(Terrain.Plains))
			.updated(pos.east(), Tile(Terrain.Water))
			.updated(pos.south(), Tile(Terrain.Forest))
			.updated(pos.west(), Tile(Terrain.Hills))
		)
		val tile = boardBaseImpl.Tile(Terrain.Plains, Terrain.Water, Terrain.Forest, Terrain.Hills, Terrain.Mountains)

		"make tiles fit" in {
			rules invokePrivate makeFit(Tile(Terrain.Mountains), board, pos) shouldBe tile
			rules invokePrivate makeFit(Tile(Terrain.Mountains), Board(), pos) shouldBe Tile(Terrain.Mountains)
		}
		"find possible positions" in {
			val board = boardBaseImpl.Board(tiles = new HashMap[Position, Tile]()
				.updated(Position(0, 0), Tile(Terrain.Plains))
				.updated(Position(1, 0), Tile(Terrain.Plains))
				.updated(Position(10, 10), Tile(Terrain.Plains))
			)

			rules invokePrivate possiblePositions(board) shouldBe Set(
				Position(0, 0).neighbours(), Position(1, 0).neighbours(), Position(10, 10).neighbours()
			).flatten.filter(p => !board.tiles.contains(p))
		}
		"decide if can place tile" in {
			rules.canPlace(tile, new HashMap[Position, Tile](), pos) shouldBe true
			rules.canPlace(tile, new HashMap[Position, Tile]().updated(Position(100, 100), Tile(Terrain.Plains)), pos) shouldBe false
			rules.canPlace(tile, new HashMap[Position, Tile]().updated(pos, Tile(Terrain.Plains)), pos) shouldBe false
			rules.canPlace(tile, board.tiles, pos) shouldBe true
			rules.canPlace(tile.copy(north = Terrain.Mountains), board.tiles, pos) shouldBe false
			rules.canPlace(tile.copy(east = Terrain.Mountains), board.tiles, pos) shouldBe false
			rules.canPlace(tile.copy(south = Terrain.Mountains), board.tiles, pos) shouldBe false
			rules.canPlace(tile.copy(west = Terrain.Mountains), board.tiles, pos) shouldBe false

			rules.canPlace(board).isFailure shouldBe true
			rules.canPlace(board.copy(currentTile = Option(tile)).placeCurrentTile(pos).get).get shouldBe true
		}
		"return random placeable tiles" in {
			noException should be thrownBy rules.randomPlaceable(Board())
			noException should be thrownBy rules.randomPlaceable(board)
		}
	}
}
