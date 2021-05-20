package de.htwg.se.tiles

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import java.io.ByteArrayInputStream

class GameSpec extends AnyWordSpec with Matchers {
	"A Game" when {
		"started" should {
			"run  without exceptions" in {
				val in = new ByteArrayInputStream("?\nplace\nh\nf\nplace\nh\ng\nf\nundo\nredo\nexit\n".getBytes)
				Console.withIn(in) {
					noException should be thrownBy Game.main(Array())
				}
			}
		}
	}
}
