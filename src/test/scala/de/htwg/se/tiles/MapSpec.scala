package de.htwg.se.tiles

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MapSpec extends AnyWordSpec with Matchers {
	"A Map" when {
		"new" should {
			val map = Map()
			"be empty" in {
				map.tiles.isEmpty shouldBe true
			}
			"return empty from toString" in {
				map.toString shouldBe "empty"
			}
		}
		"not empty" should {
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val map = Map().add((0, 0), tile).add((1, 0), tile)
			"print map" in {
				noException should be thrownBy map.toString
			}
		}
		"tile added" should {
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val map = Map().add((0, 0), tile)
			"throw if already occupied" in {
				an[Exception] should be thrownBy map.add((0, 0), tile)
			}
			"add tile" in {
				val mapNew = map.add((1, 0), tile)
				mapNew.tiles.size shouldBe map.tiles.size + 1
			}
		}
	}
}
