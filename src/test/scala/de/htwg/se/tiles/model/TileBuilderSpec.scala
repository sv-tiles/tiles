package de.htwg.se.tiles.model

import de.htwg.se.tiles.NotSoRandom
import org.scalatest.PrivateMethodTester
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TileBuilderSpec extends AnyWordSpec with Matchers with PrivateMethodTester {
	"A TileBuilder" should {
		val setRandom = PrivateMethod[Unit](Symbol("setRandom"))
		"return reproducible results" in {
			val notSoRandom1 = new NotSoRandom(0)
			TileBuilder invokePrivate setRandom(max => notSoRandom1.next(max))
			val tile1 = TileBuilder.randomTile()
			val notSoRandom2 = new NotSoRandom(0)
			TileBuilder invokePrivate setRandom(max => notSoRandom2.next(max))
			val tile2 = TileBuilder.randomTile()
			val notSoRandom3 = new NotSoRandom(0)
			TileBuilder invokePrivate setRandom(max => notSoRandom3.next(max))
			val tile3 = TileBuilder.randomTile()

			tile1 shouldBe tile2
			tile2 shouldBe tile3
		}
	}
}
