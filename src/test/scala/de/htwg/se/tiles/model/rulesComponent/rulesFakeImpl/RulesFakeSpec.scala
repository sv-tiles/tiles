package de.htwg.se.tiles.model.rulesComponent.rulesFakeImpl

import de.htwg.se.tiles.model.boardComponent.boardBaseImpl.{Board, Tile}
import de.htwg.se.tiles.model.boardComponent.{Island, Terrain}
import de.htwg.se.tiles.model.playerComponent.playerBaseImpl.PlayerBase
import de.htwg.se.tiles.model.{Direction, Position, SubPosition}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scalafx.scene.paint.Color

import scala.collection.immutable.{HashMap, HashSet}

class RulesFakeSpec extends AnyWordSpec with Matchers {
	"A RulesFake" should {
		"have equivalent non-prams constructors" in {
			RulesFake.apply().maxPeople shouldBe new RulesFake().maxPeople
		}
		"find islands" in {
			val rules = RulesFake()

			val pos = Position(0, 0)
			val players = Vector(PlayerBase("test", Color.color(1, 0, 0), people = Vector((pos, Direction.Center))))
			val board = Board(tiles = HashMap((pos, Tile(Terrain.Plains))), players = players)

			rules.findIsland(board, SubPosition(pos, Direction.Center)) shouldBe Island(HashSet(SubPosition(pos, Direction.Center),
				SubPosition(pos, Direction.North),
				SubPosition(pos, Direction.East),
				SubPosition(pos, Direction.South),
				SubPosition(pos, Direction.West)), complete = true, 2, HashSet.from(players.map(p => p.name)))
		}
		"assign points" in {
			RulesFake().assignPoints(Board(
				players = Vector(PlayerBase("test", Color.color(1, 0, 0), people = Vector((Position(0, 0), Direction.Center)))),
				islands = Vector(Island(HashSet(SubPosition(Position(0, 0), Direction.Center)), complete = true, 1, HashSet("test"))))).players.head.points shouldBe 1
		}
	}
}
