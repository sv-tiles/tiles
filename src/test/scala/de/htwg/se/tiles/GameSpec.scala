package de.htwg.se.tiles

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameSpec extends AnyWordSpec with Matchers {
	"A Game" when {
		"started" should {
			"run  without exceptions" in {
				noException should be thrownBy Game.main(Array())
			}
		}
	}
}
