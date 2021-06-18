package de.htwg.se.tiles.model.playerComponent.playerBaseImpl

import de.htwg.se.tiles.model.{Direction, Position}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scalafx.scene.paint.Color

class PlayerBaseSpec extends AnyWordSpec with Matchers {
	"A PlayerBase" when {
		"initialized" should {
			"have a name, color, people and points" in {
				val color = Color.color(1.0, 0.5, 0.25)
				val playerBase = PlayerBase("test", color)
				playerBase.name shouldBe "test"
				playerBase.color shouldBe color
				playerBase.people shouldBe Vector.empty
				playerBase.points shouldBe 0

				val people = Vector((Position(0, 0), Direction.West))
				val playerBase2 = PlayerBase("test", color, people, 100)
				playerBase2.name shouldBe "test"
				playerBase2.color shouldBe color
				playerBase2.people shouldBe people
				playerBase2.points shouldBe 100
			}
		}
	}
	"A PlayerBase" should {
		"have equivalent apply and constructor methods" in {
			val name = "test"
			val color = Color.color(1.0, 0.5, 0.25)
			PlayerBase.apply(name, color) shouldBe new PlayerBase(name, color)
		}
	}
}
