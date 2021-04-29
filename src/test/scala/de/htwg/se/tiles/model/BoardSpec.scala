package de.htwg.se.tiles.model

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class BoardSpec extends AnyWordSpec with Matchers {
	"A Map" when {
		"initialized without tiles" should {
			val map = Board()
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
				Board().toString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, false) shouldBe (" " * mapWidth + "\n") * mapHeight
			}
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val map = Board().add((0, 0), tile).add((1, 0), tile)
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
			"have the right size with frame" in {
				val lines = map.toString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, true).split("\n")
				lines.map(l => l.length).sum shouldBe (mapWidth + 2) * (mapHeight + 2)
				lines.head.length shouldBe (mapWidth + 2)
				lines.length shouldBe (mapHeight + 2)

			}
			"be nearly the same with and without highlight" in {
				map.toString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, true) shouldBe map.toString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, true, Option((0, 0))).replaceAll("[<>]", " ")
				map.toString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, false) shouldBe map.toString(offset, mapWidth, mapHeight, tileWidth, tileHeight, border, margin, false, Option((0, 0))).replaceAll("[<>]", " ")
			}
			"have correct highlighting" in {
				map.toString((0, 0), 6, 6, 5, 6, 2, 1, false, Option((0, 0))) shouldBe "  _  <\n_____<\n_____<\n_____<\n_____<\n  _  <\n"
				map.toString((0, 0), 6, 6, 5, 6, 2, 1, false, Option((1, 0))) shouldBe "  _  >\n_____>\n_____>\n_____>\n_____>\n  _  >\n"
			}
		}
		"tile added" should {
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val map = Board().add((0, 0), tile)
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
