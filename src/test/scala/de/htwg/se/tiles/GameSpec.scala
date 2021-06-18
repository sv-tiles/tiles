package de.htwg.se.tiles

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameSpec extends AnyWordSpec with Matchers {
	"A Game" when {
		"started" should {
			/*
						"run without exceptions" in {
							val in = new ByteArrayInputStream("?\naddPlayer p1\nplace\nh\nf\nplace\nh\ng\nf\nundo\nredo\nexit\n".getBytes)
							Console.withIn(in) {
								noException should be thrownBy Game.main(Array("--test"))
							}
						}
						"run without exceptions with test module" in {
							val in = new ByteArrayInputStream("?\naddPlayer p1\nplace\nh\nf\nplace\nh\ng\nf\nundo\nredo\nexit\n".getBytes)
							Console.withIn(in) {
								noException should be thrownBy Game.main(Array("--test", "--test-module"))
							}
						}*/
		}
	}
}
