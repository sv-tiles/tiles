package de.htwg.se.tiles

import com.google.inject.Guice
import de.htwg.se.tiles.control.controllerComponent.ControllerInterface
import de.htwg.se.tiles.control.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.tiles.model.fileIoComponent.FileIoInterface
import de.htwg.se.tiles.model.playerComponent.PlayerFactory
import de.htwg.se.tiles.model.playerComponent.playerBaseImpl.PlayerBase
import de.htwg.se.tiles.model.rulesComponent.RulesInterface
import de.htwg.se.tiles.model.rulesComponent.rulesBaseImpl.RulesBase
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scalafx.scene.paint.Color

class GameModuleSpec extends AnyWordSpec with Matchers {
	"A GameModule" should {
		val injector = Guice.createInjector(GameModule())
		val color = Color.color(1.0, 0.5, 0.25)
		"inject rules" in {
			injector.getInstance(classOf[RulesInterface]) shouldBe RulesBase()
		}
		"inject board" in {
			injector.getInstance(classOf[BoardInterface]) shouldBe Board()
		}
		"create players" in {
			injector.getInstance(classOf[PlayerFactory]).create("test", color) shouldBe PlayerBase("test", color)
		}
		"inject controller" in {
			val board = injector.getInstance(classOf[BoardInterface])
			val rules = injector.getInstance(classOf[RulesInterface])
			val playerFactory = injector.getInstance(classOf[PlayerFactory])
			val fileIo = injector.getInstance(classOf[FileIoInterface])

			val injected = injector.getInstance(classOf[ControllerInterface])
			val handmade = new Controller(board, rules, playerFactory, fileIo)

			injected.board shouldBe handmade.board

			injected.addPlayer("test", color)
			handmade.addPlayer("test", color)

			injected.board.players shouldBe handmade.board.players

		}
	}
}
