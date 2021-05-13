package de.htwg.se.tiles.model

import de.htwg.se.tiles.NotSoRandom
import org.scalatest.PrivateMethodTester
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TileBuilderSpec extends AnyWordSpec with Matchers with PrivateMethodTester {
	"A TileBuilder" should {
		val setRandom = PrivateMethod[Unit](Symbol("setRandom"))
		"return tiles" in {
			noException should be thrownBy TileBuilder.randomTile()

			val notSoRandom1 = new NotSoRandom(0)
			TileBuilder invokePrivate setRandom(max => notSoRandom1.next(max))
			noException should be thrownBy TileBuilder.randomTile()

		}
		"rotate tiles" in {
			val tile = Tile(Terrain.Plains, Terrain.Hills, Terrain.Forest, Terrain.Water, Terrain.Mountains)

			TileBuilder.rotateRandom(tile, 0) shouldBe tile
			TileBuilder.rotateRandom(tile, 1) shouldBe tile.rotate()
			TileBuilder.rotateRandom(tile, 2) shouldBe tile.rotate().rotate()
			TileBuilder.rotateRandom(tile, 3) shouldBe tile.rotate().rotate().rotate()
			an[IllegalArgumentException] should be thrownBy TileBuilder.rotateRandom(tile, -1)
		}
	}
}
