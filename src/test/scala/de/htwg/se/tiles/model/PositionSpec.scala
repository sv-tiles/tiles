package de.htwg.se.tiles.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PositionSpec extends AnyWordSpec with Matchers {
	"A Position" should {
		"correctly initialize" in {
			Position.unapply(Position(5, 10)).get shouldBe(5, 10)
		}
		"return neighbours" in {
			val x = 5
			val y = 10
			val pos = Position(x, y)
			pos.east() shouldBe pos.copy(x = x + 1)

			pos.west() shouldBe pos.copy(x = x - 1)

			pos.north() shouldBe pos.copy(y = y - 1)

			pos.south() shouldBe pos.copy(y = y + 1)

			pos.neighbours() shouldBe Set(pos.north(), pos.east(), pos.south(), pos.west())
		}
	}
}
