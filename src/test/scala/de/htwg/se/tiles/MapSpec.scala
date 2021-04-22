package de.htwg.se.tiles

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class MapSpec extends AnyWordSpec with Matchers {
	"A Map" when {
		"initialized without tiles" should {
			val map = Map()
			"be empty" in {
				map.tiles.isEmpty shouldBe true
			}
			"return empty from toString" in {
				map.toString shouldBe "empty"
			}
		}
		"printed" should {
			val offset = (0, 0)
			val mapWidth = 50
			val mapHeight = 20
			val tileWidth = 10
			val tileHeight = 5
			val border = 2
			val margin = 2
			"return blank if empty" in {
				Map().toString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, false) shouldBe (" " * mapWidth + "\n") * mapHeight
			}
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val map = Map().add((0, 0), tile).add((1, 0), tile)
			"not throw" in {
				noException should be thrownBy map.toString
			}
			"throw if width <=0" in {
				an[IllegalArgumentException] should be thrownBy map.toString(offset, 0, mapHeight, tileWidth, tileHeight, border, margin)
			}
			"throw if height <=0" in {
				an[IllegalArgumentException] should be thrownBy map.toString(offset, mapWidth, 0, tileWidth, tileHeight, border, margin)
			}
			"have the right size" in {
				val lines = map.toString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, false).split("\n")
				lines.map(l => l.length).sum shouldBe mapWidth * mapHeight
				lines.head.length shouldBe mapWidth
				lines.length shouldBe mapHeight

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
