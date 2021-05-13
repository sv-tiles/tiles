package de.htwg.se.tiles.model

import de.htwg.se.tiles.NotSoRandom
import org.scalatest.PrivateMethodTester
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TileBuilderSpec extends AnyWordSpec with Matchers with PrivateMethodTester {
	"A TileBuilder" should {
		val setRandom = PrivateMethod[Unit](Symbol("setRandom"))
		"return tiles" in {
			val notSoRandom1 = new NotSoRandom(0)
			TileBuilder invokePrivate setRandom(max => notSoRandom1.next(max))
			noException should be thrownBy TileBuilder.randomTile()
			
		}
	}
}
