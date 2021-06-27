package de.htwg.se.tiles.model.rulesComponent.rulesBaseImpl

import de.htwg.se.tiles.model.boardComponent.boardBaseImpl.{Board, Tile}
import de.htwg.se.tiles.model.boardComponent.{Island, Terrain, boardBaseImpl}
import de.htwg.se.tiles.model.playerComponent.playerBaseImpl.PlayerBase
import de.htwg.se.tiles.model.{Direction, Position, SubPosition}
import org.scalatest.PrivateMethodTester
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scalafx.scene.paint.Color

import scala.collection.immutable.{HashMap, HashSet}

class RulesBaseSpec extends AnyWordSpec with Matchers with PrivateMethodTester {
	"RulesBase" should {
		val rules = RulesBase()
		val makeFit = PrivateMethod[Tile](Symbol("makeFit"))
		val possiblePositions = PrivateMethod[Set[Position]](Symbol("possiblePositions"))

		val pos = Position(0, 0)
		val board = Board(tiles = new HashMap[Position, Tile]()
			.updated(pos.north(), Tile(Terrain.Plains))
			.updated(pos.east(), Tile(Terrain.Water))
			.updated(pos.south(), Tile(Terrain.Forest))
			.updated(pos.west(), Tile(Terrain.Hills)),
			players = Vector().appended(PlayerBase("test", Color.Black))
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
			rules.canPlace(board.create(currentTile = Option(tile)).placeCurrentTile(pos).get).get shouldBe true
		}
		"return random placeable tiles" in {
			noException should be thrownBy rules.randomPlaceable(Board())
			noException should be thrownBy rules.randomPlaceable(board)
		}
		"have equivalent no-prams constructors" in {
			RulesBase.apply().maxPeople shouldBe new RulesBase().maxPeople
		}
		"find islands" in {
			val pos = Position(0, 0)
			val board = Board(
				players = Vector[PlayerBase]()
					.appended(PlayerBase("p1", Color.color(0, 1, 0), people = Vector((pos, Direction.Center))))
					.appended(PlayerBase("p2", Color.color(1, 0, 0)))
				,
				tiles = new HashMap[Position, Tile]()
					.updated(pos, Tile(Terrain.Plains, Terrain.Water, Terrain.Mountains, Terrain.Water, Terrain.Plains))
					.updated(pos.north(), Tile(Terrain.Water, Terrain.Water, Terrain.Plains, Terrain.Water, Terrain.Water))
					.updated(pos.south(), Tile(Terrain.Mountains, Terrain.Water, Terrain.Mountains, Terrain.Water, Terrain.Mountains))
			)
			val rules = RulesBase()
			rules.findIsland(board, SubPosition(pos, Direction.North)) shouldBe new Island(
				HashSet(SubPosition(pos, Direction.North),
					SubPosition(pos, Direction.Center),
					SubPosition(pos.north(), Direction.South)
				), complete = true, valueOf(1, 2, rules), HashSet("p1"))

			rules.findIsland(board, SubPosition(pos, Direction.North)) shouldBe rules.findIsland(board, SubPosition(pos.north(), Direction.South))

			rules.findIsland(board, SubPosition(pos, Direction.South)) shouldBe Island(
				HashSet(SubPosition(pos, Direction.South),
					SubPosition(pos.south(), Direction.Center),
					SubPosition(pos.south(), Direction.North),
					SubPosition(pos.south(), Direction.South)
				), complete = false, valueOf(1, 3, rules), HashSet.empty)
		}
		"assignPoints" in {
			val pos = Position(0, 0)
			val board = Board(
				players = Vector[PlayerBase]()
					.appended(PlayerBase("p1", Color.color(0, 1, 0), people = Vector((pos, Direction.Center), (pos, Direction.North))))
					.appended(PlayerBase("p2", Color.color(1, 0, 0))),
				tiles = new HashMap[Position, Tile]()
					.updated(pos, Tile(Terrain.Plains, Terrain.Water, Terrain.Mountains, Terrain.Water, Terrain.Plains))
					.updated(pos.north(), Tile(Terrain.Water, Terrain.Water, Terrain.Plains, Terrain.Water, Terrain.Water))
					.updated(pos.south(), Tile(Terrain.Mountains, Terrain.Water, Terrain.Mountains, Terrain.Water, Terrain.Mountains)),
				islands = Vector(Island(HashSet(SubPosition(Position(-1, -1), Direction.Center)), complete = true, 1, HashSet.empty))
			)

			val result = RulesBase().assignPoints(board)
			val playersWithPoints = result.players

			playersWithPoints(0).points shouldBe valueOf(1, 2, rules)
			playersWithPoints(1).points shouldBe 0

			result.islands.size shouldBe 2
		}
	}

	private def valueOf(numCenter: Int, numBorder: Int, rules: RulesBase): Int = (numCenter * rules.valueCenter + numBorder * rules.valueBorder).intValue
}
