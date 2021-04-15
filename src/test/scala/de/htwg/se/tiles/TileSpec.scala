package de.htwg.se.tiles

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TileSpec extends AnyWordSpec with Matchers {
	"A Tile" when {
		"initialized" should {
			val tile = Tile(Terrain.Water, Terrain.Mountains, Terrain.Forest, Terrain.Hills, Terrain.Plains)
			"unapply" in {
				Tile.unapply(tile).get shouldBe(Terrain.Water, Terrain.Mountains, Terrain.Forest, Terrain.Hills, Terrain.Plains)
			}

			"return the center terrain symbol when toString is called" in {
				tile.toString shouldBe Terrain.Plains.symbol
			}

		}
		"printed" should {
			val tile = Tile(Terrain.Water, Terrain.Mountains, Terrain.Forest, Terrain.Hills, Terrain.Plains)
			val width = 10
			val height = 5
			val border = 2
			val margin = 2
			"throw if line number out of bounds" in {
				an[IllegalArgumentException] should be thrownBy tile.printLine(-1, width, height, border, margin)
				an[IllegalArgumentException] should be thrownBy tile.printLine(height + margin / 2, width, height, border, margin)
			}
			"throw if width too small" in {
				an[IllegalArgumentException] should be thrownBy tile.printLine(0, 2, height, border, margin)
			}
			"throw if height too small" in {
				an[IllegalArgumentException] should be thrownBy tile.printLine(0, width, 2, border, margin)
			}
			"throw if border too small" in {
				an[IllegalArgumentException] should be thrownBy tile.printLine(0, width, height, 0, margin)
			}
			"throw if border too big" in {
				an[IllegalArgumentException] should be thrownBy tile.printLine(0, width, height * width, (width - 1) / 2 + 1, margin)
				an[IllegalArgumentException] should be thrownBy tile.printLine(0, width * height, height, (height - 1) / 2 + 1, margin)
			}
			"throw if margin negative" in {
				an[IllegalArgumentException] should be thrownBy tile.printLine(0, width, height, border, -1)
			}
			"have a valid multiline representation" in {
				val t = for (x <- 0 until height + margin / 2) yield tile.printLine(x, width, height, border, margin)
				t.map(l => l.length) should contain only width + margin
				t.head.length shouldBe width + margin
				t.length shouldBe height + margin / 2
				t.map(l => l.length).sum shouldBe (height + margin / 2) * (width + margin)
			}
		}
	}
}
