package de.htwg.se.tiles.model.rulesComponent.rulesFakeImpl

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class RulesFakeSpec extends AnyWordSpec with Matchers {
	"A RulesFake" should {
		"have equivalent no-prams constructors" in {
			RulesFake.apply().maxPeople shouldBe new RulesFake().maxPeople
		}
	}
}
