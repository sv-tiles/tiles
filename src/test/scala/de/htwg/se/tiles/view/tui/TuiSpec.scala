package de.htwg.se.tiles.view.tui

import de.htwg.se.tiles.control.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.tiles.model.Position
import de.htwg.se.tiles.model.boardComponent.boardBaseImpl.{Board, Tile}
import de.htwg.se.tiles.model.boardComponent.{Terrain, boardBaseImpl}
import de.htwg.se.tiles.model.fileIoComponent.FileIoInterface
import de.htwg.se.tiles.model.fileIoComponent.fileIoFake.FileIoFake
import de.htwg.se.tiles.model.playerComponent.PlayerFactory
import de.htwg.se.tiles.model.playerComponent.playerBaseImpl.PlayerBase
import de.htwg.se.tiles.model.rulesComponent.rulesFakeImpl.RulesFake
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scalafx.scene.paint.Color

import scala.collection.immutable.HashMap

class TuiSpec extends AnyWordSpec with Matchers {
	def noc(f: => Any): Unit = {
		Console.withOut((b: Int) => {}) {
			f
		}
	}

	def playerGenerator: PlayerFactory = (name: String, color: Color) => PlayerBase(name, color)

	def fileIo: FileIoInterface = new FileIoFake()

	"A Tui" when {
		"initializing" should {

			"throw exception when scale < 3" in noc {
				an[IllegalArgumentException] should be thrownBy new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), 1, 1, 2, (0, 0))
			}
			"throw exception when width < 1" in noc {
				an[IllegalArgumentException] should be thrownBy new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), 0, 1, 3, (0, 0))
			}
			"throw exception when height < 1" in noc {
				an[IllegalArgumentException] should be thrownBy new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), 1, 0, 3, (0, 0))
			}
		}
		"initialized" should noc {

			val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)

			val width = 50
			val height = 30
			val scale = 3

			"add player" in noc {
				val controller = new Controller(Board(), RulesFake(), playerGenerator, fileIo = fileIo)
				val tui = new Tui(controller, width, height, scale, (0, 0))
				tui.command("addPlayer p1")
				controller.board.players.length shouldBe 1
				controller.board.players(0).name shouldBe "p1"
			}
			"move offset (x/y) by 1 on 'w', 'a', 's', 'd'" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))
				val original = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("w").isEmpty shouldBe true
				tui.offset._2 shouldBe original.offset._2 - 1
				tui.command("a").isEmpty shouldBe true
				tui.offset._1 shouldBe original.offset._1 - 1
				tui.command("s").isEmpty shouldBe true
				tui.offset._2 shouldBe original.offset._2
				tui.command("d").isEmpty shouldBe true
				tui.offset._1 shouldBe original.offset._1
			}
			"move cursor (x/y) by 1 on 't', 'f', 'g', 'h'" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))
				val original = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("t").isEmpty shouldBe true
				tui.cursor._2 shouldBe original.cursor._2 - 1
				tui.command("f").isEmpty shouldBe true
				tui.cursor._1 shouldBe original.cursor._1 - 1
				tui.command("g").isEmpty shouldBe true
				tui.cursor._2 shouldBe original.cursor._2
				tui.command("h").isEmpty shouldBe true
				tui.cursor._1 shouldBe original.cursor._1
			}
			"set offset on 'position <x> <y>'" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("position 10 20").isEmpty shouldBe true
				tui.offset shouldBe(10, 20)
			}
			"set cursor to offset on 'cursor reset'" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (1, 10))

				tui.command("cursor reset").isEmpty shouldBe true
				tui.cursor shouldBe tui.offset
			}
			"get offset on 'position'" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("position").get shouldBe "Position: " + tui.offset._1 + " " + tui.offset._2
			}
			"rotate tiles on 'q' and 'r' counterclockwise" in noc {
				val tile1 = boardBaseImpl.Tile(Terrain.Plains, Terrain.Water, Terrain.Forest, Terrain.Hills, Terrain.Mountains)
				val controller = new Controller(Board(tiles = new HashMap().updated(Position(0, 0), tile1), currentTile = Option.empty, currentPos = Option(Position(0, 0))), RulesFake(), playerGenerator, fileIo = fileIo)
				val tui = new Tui(controller, width, height, scale, (0, 0))

				tui.command("q").isEmpty shouldBe true
				controller.board.tiles(Position(0, 0)) shouldBe tile1.rotate(false)
				tui.command("r").isEmpty shouldBe true
				controller.board.tiles(Position(0, 0)) shouldBe tile1.rotate(false).rotate(false)
			}
			"rotate tiles on 'e' and 'z' clockwise" in noc {
				val tile1 = boardBaseImpl.Tile(Terrain.Plains, Terrain.Water, Terrain.Forest, Terrain.Hills, Terrain.Mountains)
				val controller = new Controller(Board(tiles = new HashMap().updated(Position(0, 0), tile1), currentTile = Option.empty, currentPos = Option(Position(0, 0))), RulesFake(), playerGenerator, fileIo = fileIo)
				val tui = new Tui(controller, width, height, scale, (0, 0))

				tui.command("e").isEmpty shouldBe true
				controller.board.tiles(Position(0, 0)) shouldBe tile1.rotate(true)
				tui.command("z").isEmpty shouldBe true
				controller.board.tiles(Position(0, 0)) shouldBe tile1.rotate(true).rotate(true)
			}
			"set scale to 'f' on 'scale <f>" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("scale 10").isEmpty shouldBe true
				tui.scale shouldBe 10
				tui.command("scale 20").isEmpty shouldBe true
				tui.scale shouldBe 20
			}
			"get scale on 'scale'" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("scale").get shouldBe "Scale: " + tui.scale
			}
			"set width to 'n' on 'width <n>" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("width 200").isEmpty shouldBe true
				tui.width shouldBe 200
			}
			"get width on 'width'" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("width").get shouldBe "Width: " + tui.width
			}
			"set height to 'n' on 'height <n>" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("height 200").isEmpty shouldBe true
				tui.height shouldBe 200
			}
			"get height on 'height'" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("height").get shouldBe "Height: " + tui.height
			}
			"set size to 'width', 'height' on 'size <width> <height>" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("size 200 100").isEmpty shouldBe true
				tui.width shouldBe 200
				tui.height shouldBe 100
			}
			"get size on 'size'" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("size").get shouldBe "Size: " + tui.width + " " + tui.height
			}
			"should not do anything on unknown command" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				val command = "unknownCoMmaNd"
				tui.command(command).get shouldBe "Unknown command: " + command
			}
			"return 'stopping' on 'exit'" in noc {
				val tui = new Tui(new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo), width, height, scale, (0, 0))

				tui.command("exit").get shouldBe "stopping"
			}
			"print view" in noc {
				val controller = new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo)
				val tui = new Tui(controller, width, height, scale, (0, 0))

				tui.getView shouldBe controller.mapToString(tui.offset, tui.width, tui.height, tui.scale * 2, tui.scale, Math.max(1, tui.scale * .2).intValue, 2, true, Option(tui.cursor)) + "\n" +
					controller.currentTileToString(tui.scale * 2, tui.scale, Math.max(1, tui.scale * .2).intValue, 2).get.trim() + "\n\n"
				tui.cursor = (1, 0)
				tui.getView shouldBe controller.mapToString(tui.offset, tui.width, tui.height, tui.scale * 2, tui.scale, Math.max(1, tui.scale * .2).intValue, 2, true, Option(1, 0)) + "\n" +
					controller.currentTileToString(tui.scale * 2, tui.scale, Math.max(1, tui.scale * .2).intValue, 2).get.trim() + "\n\n"
			}
			"update" in noc {
				val controller = new Controller(Board(), RulesFake(), playerFactory = playerGenerator, fileIo = fileIo)
				val tui = new Tui(controller, width, height, scale, (0, 0))

				tui.command("size")
				noException should be thrownBy tui.update((true, ""))
				noException should be thrownBy tui.update((true, "Already occupied"))
			}
			"reset board on 'clear'" in noc {
				val controller = new Controller(Board(tiles = new HashMap().updated(Position(10, 10), tile).updated(Position(0, 0), tile)), RulesFake(), playerGenerator, fileIo = fileIo)
				val tui = new Tui(controller, width, height, scale, (0, 0))

				tui.command("clear")
				controller.board shouldBe Board().create(currentTile = controller.board.currentTile)
			}
			"undo redo" in noc {
				val controller = new Controller(Board(), RulesFake(), playerGenerator, fileIo = fileIo)
				val tui = new Tui(controller, width, height, scale, (0, 0))
				tui.command("addPlayer test")
				tui.command("h")

				val boardBeforePlace = controller.board
				tui.command("place")
				controller.board should not be boardBeforePlace
				val boardAfterPlace = controller.board
				tui.command("undo")
				controller.board shouldBe boardBeforePlace
				tui.command("redo")
				controller.board shouldBe boardAfterPlace

				tui.command("h")
				tui.command("place")

				val board2 = controller.board

				tui.command("undo")
				tui.command("undo")

				controller.board shouldBe boardAfterPlace

				tui.command("redo")
				tui.command("redo")

				controller.board shouldBe board2
			}
			"save and load game" in noc {
				val controller = new Controller(Board(), RulesFake(), playerGenerator, fileIo = fileIo)
				val tui = new Tui(controller, width, height, scale, (0, 0))

				controller.placeTile(Position(1, 1))

				tui.command("save test")

				val board = controller.board

				controller.clear()

				controller.board should not be board

				tui.command("load test")

				controller.board shouldBe board
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
