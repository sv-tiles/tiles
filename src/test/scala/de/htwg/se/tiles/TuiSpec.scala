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
			val tui = Tui(50, 30, 3, (0, 0), map)
			"unapply" in {
				Tui.unapply(tui).get shouldBe(50, 30, 3, (0, 0), map)
			}
			"move offset (x/y) by 1 on 'w', 'a', 's', 'd'" in {
				testNoMsg(tui.command("w")).offset._2 shouldBe tui.offset._2 - 1
				testNoMsg(tui.command("a")).offset._1 shouldBe tui.offset._1 - 1
				testNoMsg(tui.command("s")).offset._2 shouldBe tui.offset._2 + 1
				testNoMsg(tui.command("d")).offset._1 shouldBe tui.offset._1 + 1
			}
			"set scale to 'f' on 'scale <f>" in {
				testNoMsg(tui.command("scale 10")).scale shouldBe 10
				testNoMsg(tui.command("scale 20")).scale shouldBe 20
			}
			"generate random tile at 'x' 'y' on 'rand <x> <y>'" in {
				Array((3, 4), (7, 2)).foreach(p => {
					tui.map.tiles.contains(p) shouldBe false
					testNoMsg(tui.command(s"rand ${p._1} ${p._2}")).map.tiles.contains(p) shouldBe true
				})
			}
			"set width to 'n' on 'width <n>" in {
				testNoMsg(tui.command("width 200")).width shouldBe 200
			}
			"set height to 'n' on 'height <n>" in {
				testNoMsg(tui.command("height 200")).height shouldBe 200
			}
			"should not do anything on unknown command" in {
				val command = "unknownCoMmaNd"
				val (newTui, msg) = tui.command(command)
				newTui shouldBe tui
				msg.isDefined shouldBe true
				msg.get shouldBe "Unknown command: " + command
			}
			"return 'stopping' on 'exit'" in {
				val (newTui, msg) = tui.command("exit")
				newTui shouldBe tui
				msg.isDefined shouldBe true
				msg.get shouldBe "stopping"
			}
		}
	}

	def testNoMsg(r: (Tui, Option[String])): Tui = {
		r._2.isDefined shouldBe false
		r._1
	}
}
