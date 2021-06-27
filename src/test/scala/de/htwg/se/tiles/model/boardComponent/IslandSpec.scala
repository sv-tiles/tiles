package de.htwg.se.tiles.model.boardComponent

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.HashSet

class IslandSpec extends AnyWordSpec with Matchers {
	"A Island" when {
		"initialized" should {
			"fail if content empty" in {
				an[IllegalArgumentException] should be thrownBy Island(HashSet.empty, complete = false, 0, HashSet.empty)
			}
		}
	}
}
