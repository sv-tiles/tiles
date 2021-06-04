package de.htwg.se.tiles.util

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class Position2DSpec extends AnyWordSpec with Matchers {
	"A Position2D" should {
		"do simple math" in {
			val pos1 = Position2D(1, 10)
			val pos2 = Position2D(100, 1000)

			pos1 + pos2 shouldBe Position2D(101, 1010)
			pos2 - pos1 shouldBe Position2D(99, 990)

			pos1 + (100, 1000) shouldBe Position2D(101, 1010)
			pos2 - (1, 10) shouldBe Position2D(99, 990)
		}
	}
}
