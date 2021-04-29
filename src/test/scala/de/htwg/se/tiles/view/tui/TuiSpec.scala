package de.htwg.se.tiles.view.tui

import de.htwg.se.tiles.control.Controller
import de.htwg.se.tiles.model.{Board, Terrain, Tile}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.collection.immutable.HashMap

class TuiSpec extends AnyWordSpec with Matchers {
	"A Tui" when {
		"initializing" should {

			"throw exception when scale < 3" in {
				an[IllegalArgumentException] should be thrownBy new Tui(new Controller(), 1, 1, 2, (0, 0))
			}
			"throw exception when width < 1" in {
				an[IllegalArgumentException] should be thrownBy new Tui(new Controller(), 0, 1, 3, (0, 0))
			}
			"throw exception when height < 1" in {
				an[IllegalArgumentException] should be thrownBy new Tui(new Controller(), 1, 0, 3, (0, 0))
			}
		}
		"initialized" should {
			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)

			val width = 50
			val height = 30
			val scale = 3


			"move offset (x/y) by 1 on 'w', 'a', 's', 'd'" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))
				val original = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("w").isEmpty shouldBe true
				tui.offset._2 shouldBe original.offset._2 - 1
				tui.command("a").isEmpty shouldBe true
				tui.offset._1 shouldBe original.offset._1 - 1
				tui.command("s").isEmpty shouldBe true
				tui.offset._2 shouldBe original.offset._2
				tui.command("d").isEmpty shouldBe true
				tui.offset._1 shouldBe original.offset._1
			}
			"move cursor (x/y) by 1 on 't', 'f', 'g', 'h'" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))
				val original = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("t").isEmpty shouldBe true
				tui.cursor._2 shouldBe original.cursor._2 - 1
				tui.command("f").isEmpty shouldBe true
				tui.cursor._1 shouldBe original.cursor._1 - 1
				tui.command("g").isEmpty shouldBe true
				tui.cursor._2 shouldBe original.cursor._2
				tui.command("h").isEmpty shouldBe true
				tui.cursor._1 shouldBe original.cursor._1
			}
			"move cursor (x/y) by 1 on 't', 'f', 'g', 'h' and return 'OCCUPIED'" in {
				val board = Board(new HashMap().updated((0, 0), tile).updated((0, -1), tile).updated((-1, -1), tile).updated((-1, 0), tile))
				val tui = new Tui(new Controller(board), width, height, scale, (0, 0))
				val original = new Tui(new Controller(board), width, height, scale, (0, 0))

				tui.command("t").get shouldBe "OCCUPIED"
				tui.cursor._2 shouldBe original.cursor._2 - 1
				tui.command("f").get shouldBe "OCCUPIED"
				tui.cursor._1 shouldBe original.cursor._1 - 1
				tui.command("g").get shouldBe "OCCUPIED"
				tui.cursor._2 shouldBe original.cursor._2
				tui.command("h").get shouldBe "OCCUPIED"
				tui.cursor._1 shouldBe original.cursor._1
			}
			"set offset on 'position <x> <y>'" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("position 10 20").isEmpty shouldBe true
				tui.offset shouldBe(10, 20)
			}
			"set cursor to offset on 'cursor reset'" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (1, 10))

				tui.command("cursor reset").isEmpty shouldBe true
				tui.cursor shouldBe tui.offset
			}
			"set cursor to offset on 'cursor reset' and return 'OCCUPIED" in {
				val tui = new Tui(new Controller(Board(new HashMap().updated((1, 10), tile))), width, height, scale, (1, 10))

				tui.command("cursor reset").get shouldBe "OCCUPIED"
				tui.cursor shouldBe tui.offset
			}
			"get offset on 'position'" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("position").get shouldBe "Position: " + tui.offset._1 + " " + tui.offset._2
			}
			"rotate tiles on 'q' and 'r' counterclockwise" in {
				val tile1 = Tile(Terrain.Plains, Terrain.Water, Terrain.Forest, Terrain.Hills, Terrain.Mountains)
				val controller = new Controller(Board(new HashMap().updated((0, 0), tile1), Option.empty, Option(0, 0)))
				val tui = new Tui(controller, width, height, scale, (0, 0))

				tui.command("q").isEmpty shouldBe true
				controller.board.tiles(0, 0) shouldBe tile1.rotate(false)
				tui.command("r").isEmpty shouldBe true
				controller.board.tiles(0, 0) shouldBe tile1.rotate(false).rotate(false)
			}
			"rotate tiles on 'e' and 'z' clockwise" in {
				val tile1 = Tile(Terrain.Plains, Terrain.Water, Terrain.Forest, Terrain.Hills, Terrain.Mountains)
				val controller = new Controller(Board(new HashMap().updated((0, 0), tile1), Option.empty, Option(0, 0)))
				val tui = new Tui(controller, width, height, scale, (0, 0))

				tui.command("e").isEmpty shouldBe true
				controller.board.tiles(0, 0) shouldBe tile1.rotate(true)
				tui.command("z").isEmpty shouldBe true
				controller.board.tiles(0, 0) shouldBe tile1.rotate(true).rotate(true)
			}
			"set scale to 'f' on 'scale <f>" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("scale 10").isEmpty shouldBe true
				tui.scale shouldBe 10
				tui.command("scale 20").isEmpty shouldBe true
				tui.scale shouldBe 20
			}
			"get scale on 'scale'" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("scale").get shouldBe "Scale: " + tui.scale
			}
			"set width to 'n' on 'width <n>" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("width 200").isEmpty shouldBe true
				tui.width shouldBe 200
			}
			"get width on 'width'" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("width").get shouldBe "Width: " + tui.width
			}
			"set height to 'n' on 'height <n>" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("height 200").isEmpty shouldBe true
				tui.height shouldBe 200
			}
			"get height on 'height'" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("height").get shouldBe "Height: " + tui.height
			}
			"set size to 'width', 'height' on 'size <width> <height>" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("size 200 100").isEmpty shouldBe true
				tui.width shouldBe 200
				tui.height shouldBe 100
			}
			"get size on 'size'" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("size").get shouldBe "Size: " + tui.width + " " + tui.height
			}
			"should not do anything on unknown command" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				val command = "unknownCoMmaNd"
				tui.command(command).get shouldBe "Unknown command: " + command
			}
			"return 'stopping' on 'exit'" in {
				val tui = new Tui(new Controller(Board()), width, height, scale, (0, 0))

				tui.command("exit").get shouldBe "stopping"
			}
			"print view" in {
				val controller = new Controller(Board())
				val tui = new Tui(controller, width, height, scale, (0, 0))

				tui.getView shouldBe controller.mapToString(tui.offset, tui.width, tui.height, tui.scale * 2, tui.scale, Math.max(1, tui.scale * .2).intValue, 2, true, Option(tui.cursor)) + "\n" +
					controller.currentTileToString(tui.scale * 2, tui.scale, Math.max(1, tui.scale * .2).intValue, 2).trim() + "\n\n"
				tui.cursor = (1, 0)
				tui.getView shouldBe controller.mapToString(tui.offset, tui.width, tui.height, tui.scale * 2, tui.scale, Math.max(1, tui.scale * .2).intValue, 2, true, Option(1, 0)) + "\n" +
					controller.currentTileToString(tui.scale * 2, tui.scale, Math.max(1, tui.scale * .2).intValue, 2).trim() + "\n\n"
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
