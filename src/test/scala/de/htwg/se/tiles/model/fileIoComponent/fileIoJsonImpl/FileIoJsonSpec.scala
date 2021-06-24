package de.htwg.se.tiles.model.fileIoComponent.fileIoJsonImpl

import de.htwg.se.tiles.model.boardComponent.Terrain
import de.htwg.se.tiles.model.boardComponent.boardBaseImpl.{Board, Tile, TileBuilder}
import de.htwg.se.tiles.model.playerComponent.playerBaseImpl.PlayerBase
import de.htwg.se.tiles.model.{Direction, Position}
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import scalafx.scene.paint.Color

import java.io.File
import scala.collection.immutable.HashMap

class FileIoJsonSpec extends AnyWordSpec with Matchers {
	"A FileIoJson" should {
		val fio = new FileIoJson()
		new File("out").mkdir()

		"save and load an empty board" in {
			val board = Board()

			fio.save("out/empty.json", board).isSuccess shouldBe true

			val loadResult = fio.load("out/empty.json")

			loadResult.isSuccess shouldBe true

			loadResult.get shouldBe board
		}
		"save and load a full board" in {
			val boringtile = Tile(Terrain.Plains)

			val player = PlayerBase("test1", Color.Red)

			val board = Board(
				players = Vector().appended(player.copy(people = player.people.appended((Position(0, 0), Direction.Center)))).appended(PlayerBase("test2", Color.Blue)),
				currentPlayer = 1,
				currentTile = Option(TileBuilder.randomTile()),
				currentPos = Option.empty,
				tiles = HashMap().updated(Position(0, 0), boringtile).updated(Position(1, 0), boringtile).updated(Position(0, 1), boringtile)
			)

			fio.save("out/full.json", board).isSuccess shouldBe true

			val loadResult = fio.load("out/full.json")

			loadResult.isSuccess shouldBe true

			loadResult.get shouldBe board

			val board2 = board.placeCurrentTile(Position(1, 1)).get

			fio.save("out/full2.json", board2).isSuccess shouldBe true

			val loadResult2 = fio.load("out/full2.json")

			loadResult2.isSuccess shouldBe true

			loadResult2.get shouldBe board2
		}
	}
}
