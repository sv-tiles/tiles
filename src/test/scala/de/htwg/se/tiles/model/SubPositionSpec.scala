package de.htwg.se.tiles.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class SubPositionSpec extends AnyWordSpec with Matchers {
	"A SubPosition" should {
		"return neighbours of center" in {
			val p = SubPosition(Position(0, 0), Direction.Center)

			p.north() shouldBe p.copy(direction = Direction.North)
			p.east() shouldBe p.copy(direction = Direction.East)
			p.south() shouldBe p.copy(direction = Direction.South)
			p.west() shouldBe p.copy(direction = Direction.West)

			p.neighbours() shouldBe Set(p.north(), p.south(), p.east(), p.west())
		}
		"return neighbours of north" in {
			val p = SubPosition(Position(0, 0), Direction.North)
			val pNorth = Position(0, -1)

			p.north() shouldBe p.copy(position = pNorth, direction = Direction.South)
			p.east() shouldBe p.copy(direction = Direction.East)
			p.south() shouldBe p.copy(direction = Direction.Center)
			p.west() shouldBe p.copy(direction = Direction.West)

			p.neighbours() shouldBe Set(p.north(), p.south(), p.east(), p.west())
		}
		"return neighbours of east" in {
			val p = SubPosition(Position(0, 0), Direction.East)
			val pEast = Position(1, 0)

			p.north() shouldBe p.copy(direction = Direction.North)
			p.east() shouldBe p.copy(pEast, direction = Direction.West)
			p.south() shouldBe p.copy(direction = Direction.South)
			p.west() shouldBe p.copy(direction = Direction.Center)

			p.neighbours() shouldBe Set(p.north(), p.south(), p.east(), p.west())
		}
		"return neighbours of south" in {
			val p = SubPosition(Position(0, 0), Direction.South)
			val pSouth = Position(0, 1)

			p.north() shouldBe p.copy(direction = Direction.Center)
			p.east() shouldBe p.copy(direction = Direction.East)
			p.south() shouldBe p.copy(pSouth, direction = Direction.North)
			p.west() shouldBe p.copy(direction = Direction.West)

			p.neighbours() shouldBe Set(p.north(), p.south(), p.east(), p.west())
		}
		"return neighbours of west" in {
			val p = SubPosition(Position(0, 0), Direction.West)
			val pWest = Position(-1, 0)

			p.north() shouldBe p.copy(direction = Direction.North)
			p.east() shouldBe p.copy(direction = Direction.Center)
			p.south() shouldBe p.copy(direction = Direction.South)
			p.west() shouldBe p.copy(pWest, direction = Direction.East)

			p.neighbours() shouldBe Set(p.north(), p.south(), p.east(), p.west())
		}
	}
}
