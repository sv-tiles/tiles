package de.htwg.se.tiles.model.boardComponent.boardBaseImpl

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class PlacementExceptionSpec extends AnyWordSpec with Matchers {
	"A PlacementException" when {
		"initialized" should {
			"have a message" in {
				val thrown = the[PlacementException] thrownBy (throw PlacementException("test"))
				thrown.getMessage shouldBe "test"
			}
			"unapply" in {
				PlacementException.unapply(PlacementException("test")).get shouldBe "test"
			}
		}
	}
}
