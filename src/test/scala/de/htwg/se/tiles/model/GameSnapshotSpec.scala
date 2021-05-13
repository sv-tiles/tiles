package de.htwg.se.tiles.model

import de.htwg.se.tiles.control.Controller
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec

class GameSnapshotSpec extends AnyWordSpec with Matchers {
	"A GameSnapshot" should {
		"be used to store and restore a controller" in {
			val controller = new Controller()
			controller.placeTile((0, 0))
			controller.commit()
			controller.placeTile(1, 0)

			val snapshot = GameSnapshot.fromController(controller)

			snapshot.toController.board shouldBe controller.board
		}
	}
}
