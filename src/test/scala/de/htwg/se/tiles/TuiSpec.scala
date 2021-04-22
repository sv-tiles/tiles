package de.htwg.se.tiles

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TuiSpec extends AnyWordSpec with Matchers {
	"A Tui" when {
		"initializing" should {
			"throw exception when scale < 3" in {
				an[IllegalArgumentException] should be thrownBy Tui(1, 1, 2, (0, 0), Map())
			}
			"throw exception when width < 1" in {
				an[IllegalArgumentException] should be thrownBy Tui(0, 1, 3, (0, 0), Map())
			}
			"throw exception when height < 1" in {
				an[IllegalArgumentException] should be thrownBy Tui(1, 0, 3, (0, 0), Map())
			}
		}
		"initialized" should {
			val map = Map()
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val tui = Tui(50, 30, 6, (0, 0), map)
			"move offset (x/y) by 1 on 'w', 'a', 's', 'd'" in {
				tui.command("w").offset._2 shouldBe tui.offset._2 - 1
				tui.command("a").offset._1 shouldBe tui.offset._1 - 1
				tui.command("s").offset._2 shouldBe tui.offset._2 + 1
				tui.command("d").offset._1 shouldBe tui.offset._1 + 1
			}
		}
	}
}
