package de.htwg.se.tiles.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TerrainSpec extends AnyWordSpec with Matchers {
	"A Terrain" should {
		"have defaults" in {
			Terrain.defaults should contain allElementsOf Vector(Terrain.Water, Terrain.Forest, Terrain.Hills, Terrain.Plains, Terrain.Mountains)
		}

	}
	"A Terrain's symbol" should {
		"consist of only one letter" in {
			Terrain.defaults.filter(t => t.symbol.length != 1) shouldBe empty
		}
		"should be used by toString" in {
			Terrain.Mountains.toString shouldBe Terrain.Mountains.symbol
		}
	}
}
