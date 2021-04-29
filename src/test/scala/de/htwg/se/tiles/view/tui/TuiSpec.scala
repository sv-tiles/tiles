package de.htwg.se.tiles.view.tui

import de.htwg.se.tiles.model.{Map, Terrain, Tile}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class TuiSpec extends AnyWordSpec with Matchers {
	"A Tui" when {
		"initializing" should {

			"throw exception when scale < 3" in {
				an[IllegalArgumentException] should be thrownBy Tui(1, 1, 2, (0, 0), Map(), Option.empty)
			}
			"throw exception when width < 1" in {
				an[IllegalArgumentException] should be thrownBy Tui(0, 1, 3, (0, 0), Map(), Option.empty)
			}
			"throw exception when height < 1" in {
				an[IllegalArgumentException] should be thrownBy Tui(1, 0, 3, (0, 0), Map(), Option.empty)
			}
		}
		"initialized" should {
			val map = Map()
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
			val tui = Tui(50, 30, 3, (0, 0), map, Option.empty)
			"unapply" in {
				Tui.unapply(tui).get shouldBe(50, 30, 3, (0, 0), map, Option.empty)
			}
			"move offset (x/y) by 1 on 'w', 'a', 's', 'd'" in {
				testNoMsg(tui.command("w")).offset._2 shouldBe tui.offset._2 - 1
				testNoMsg(tui.command("a")).offset._1 shouldBe tui.offset._1 - 1
				testNoMsg(tui.command("s")).offset._2 shouldBe tui.offset._2 + 1
				testNoMsg(tui.command("d")).offset._1 shouldBe tui.offset._1 + 1
			}
			"set offset on 'position <x> <y>'" in {
				testNoMsg(tui.command("position 10 20")).offset shouldBe(10, 20)
			}
			"get offset on 'position'" in {
				testOnlyMsg(tui, tui.command("position")) shouldBe "Position: " + tui.offset._1 + " " + tui.offset._2
			}
			"set scale to 'f' on 'scale <f>" in {
				testNoMsg(tui.command("scale 10")).scale shouldBe 10
				testNoMsg(tui.command("scale 20")).scale shouldBe 20
			}
			"get scale on 'scale'" in {
				testOnlyMsg(tui, tui.command("scale")) shouldBe "Scale: " + tui.scale
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
			"get width on 'width'" in {
				testOnlyMsg(tui, tui.command("width")) shouldBe "Width: " + tui.width
			}
			"set height to 'n' on 'height <n>" in {
				testNoMsg(tui.command("height 200")).height shouldBe 200
			}
			"get height on 'height'" in {
				testOnlyMsg(tui, tui.command("height")) shouldBe "Height: " + tui.height
			}
			"set size to 'width', 'height' on 'size <width> <height>" in {
				val newTui = testNoMsg(tui.command("size 200 100"))
				newTui.width shouldBe 200
				newTui.height shouldBe 100
			}
			"get size on 'size'" in {
				testOnlyMsg(tui, tui.command("size")) shouldBe "Size: " + tui.width + " " + tui.height
			}
			"set highlight on 'highlight <x> <y>'" in {
				val r = testNoMsg(tui.command("highlight 0 0")).highlight
				r.isDefined shouldBe true
				r.get shouldBe(0, 0)
			}
			"reset highlight on 'highlight none'" in {
				val tui2 = tui.copy(highlight = Option((0, 0)))
				testNoMsg(tui2.command("highlight none")).highlight.isDefined shouldBe false
			}
			"get highlight on 'highlight'" in {
				val tui2 = tui.copy(highlight = Option.empty)
				testOnlyMsg(tui2, tui2.command("highlight")) shouldBe "Highlight: none"
				val tui3 = tui.copy(highlight = Option((0, 0)))
				testOnlyMsg(tui3, tui3.command("highlight")) shouldBe "Highlight: 0 0"
			}
			"should not do anything on unknown command" in {
				val command = "unknownCoMmaNd"
				testOnlyMsg(tui, tui.command(command)) shouldBe "Unknown command: " + command
			}
			"return 'stopping' on 'exit'" in {
				testOnlyMsg(tui, tui.command("exit")) shouldBe "stopping"
			}
			"print map" in {
				tui.getMapView shouldBe map.toString(tui.offset, tui.width, tui.height, tui.scale * 2, tui.scale, Math.max(1, tui.scale * .2).intValue, 2, true, Option.empty)
				val tui2 = tui.copy(highlight = Option(1, 0))
				tui2.getMapView shouldBe map.toString(tui.offset, tui.width, tui.height, tui.scale * 2, tui.scale, Math.max(1, tui.scale * .2).intValue, 2, true, Option(1, 0))
			}
		}
	}

	def testNoMsg(r: (Tui, Option[String])): Tui = {
		r._2.isDefined shouldBe false
		r._1
	}

	def testOnlyMsg(tui: Tui, r: (Tui, Option[String])): String = {
		r._1 shouldBe tui
		r._2.isDefined shouldBe true
		r._2.get
	}
}
