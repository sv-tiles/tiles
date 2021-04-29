package de.htwg.se.tiles.model

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

			"be able to rotate" in {
				val rotatedClockwise = Tile(tile.west, tile.north, tile.east, tile.south, tile.center)
				val rotatedCounterclockwise = Tile(tile.east, tile.south, tile.west, tile.north, tile.center)

				tile.rotate(true) shouldBe rotatedClockwise
				tile.rotate(true).rotate(true) shouldBe rotatedClockwise.rotate(true)
				tile.rotate(true).rotate(true).rotate(true) shouldBe rotatedClockwise.rotate(true).rotate(true)
				tile.rotate(true).rotate(true).rotate(true).rotate(true) shouldBe tile

				tile.rotate(false) shouldBe rotatedCounterclockwise
				tile.rotate(false).rotate(false) shouldBe rotatedCounterclockwise.rotate(false)
				tile.rotate(false).rotate(false).rotate(false) shouldBe rotatedCounterclockwise.rotate(false).rotate(false)
				tile.rotate(false).rotate(false).rotate(false).rotate(false) shouldBe tile
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
			"have a single tile multiline representation" in {
				tile.tileToString(width, height, border, margin) shouldBe (for (i <- 0 until 5) yield tile.printLine(i, width, height, border, margin)).mkString("\n")
			}
		}
	}
	"A Tile" should {
		"return a random tile on random" in {
			noException should be thrownBy Tile.random()
		}
	}
}
