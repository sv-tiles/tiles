package de.htwg.se.tiles.control.controllerComponent.controllerBaseImpl

import com.google.inject.Inject
import de.htwg.se.tiles.control.controllerComponent.ControllerInterface
import de.htwg.se.tiles.model._
import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.model.fileIoComponent.FileIoInterface
import de.htwg.se.tiles.model.playerComponent.PlayerFactory
import de.htwg.se.tiles.model.rulesComponent.RulesInterface
import de.htwg.se.tiles.util.UndoManager
import scalafx.scene.paint.Color

import scala.util.{Success, Try}


class Controller @Inject()(var board: BoardInterface, var rules: RulesInterface, var playerFactory: PlayerFactory, var fileIo: FileIoInterface, var undoManager: UndoManager = new UndoManager()) extends ControllerInterface {
	override def addPlayer(name: String, color: Color = Color.Black): Unit = {
		board = board.updatePlayers(board.players.appended(playerFactory.create(name, color)))
		notifyObservers((true, ""))
	}

	override def load(file: String): Unit = notifyObservers(Try({
		board = fileIo.load(file).get
	}).fold(err => (false, "Error:\n" + err.getMessage), _ => (true, "Success")))

	override def save(file: String): Unit = notifyObservers(fileIo.save(file, board).fold(err => (false, "Error:\n" + err.getMessage), _ => (true, "Success")))

	override def clear(): Unit = {
		undoManager.execute(new ClearCommand(this))
		notifyObservers((true, ""))
	}

	override def placeTile(pos: (Int, Int)): Unit =
		placeTile(Position(pos._1, pos._2))

	override def placeTile(pos: Position): Unit =
		undoManager.execute(new PlaceTileCommand(this, pos)).fold(
			e => notifyObservers((false, e.getMessage)),
			_ => notifyObservers((true, ""))
		)

	override def pickUpTile(): Unit =
		undoManager.execute(new PickUpTileCommand(this)).fold(
			e => notifyObservers((false, e.getMessage)),
			_ => notifyObservers((true, ""))
		)

	override def rotate(clockwise: Boolean): Unit = {
		undoManager.execute(new RotateCommand(this, clockwise))
		notifyObservers((true, ""))
	}

	override def commit(placePeople: Option[Direction]): Unit =
		undoManager.execute(new CommitCommand(this, rules, placePeople)).fold(
			e => notifyObservers((false, e.getMessage)),
			_ => notifyObservers((true, ""))
		)

	override def mapToString(offset: (Int, Int), mapWidth: Int, mapHeight: Int, tileWidth: Int, tileHeight: Int, border: Int, margin: Int, frame: Boolean = true, highlight: Option[(Int, Int)] = Option.empty): String =
		board.boardToString(Position(offset._1, offset._2), mapWidth, mapHeight, tileWidth, tileHeight, border, margin, frame, highlight.map(pos => Position(pos._1, pos._2))).recover(e => e.getMessage).get

	override def currentTileToString(width: Int, height: Int, border: Int, margin: Int): Try[String] =
		board.currentTile.map[Try[String]](t => t.tileToString(width, height, border, margin)).getOrElse(Success("Tile placed"))

	override def toSnapshot: GameSnapshot = GameSnapshot(board)

	override def restoreSnapshot(snapshot: GameSnapshot): Controller = {
		board = snapshot.board
		this
	}

	override def undo(): Unit =
		undoManager.undo().fold(e => notifyObservers((false, e.getMessage)), _ => notifyObservers((true, "")))

	override def redo(): Unit =
		undoManager.redo().fold(e => notifyObservers((false, e.getMessage)), _ => notifyObservers((true, "")))
}
