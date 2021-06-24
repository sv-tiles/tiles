package de.htwg.se.tiles.model.fileIoComponent.fileIoErrorImpl

import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.model.fileIoComponent.FileIoInterface

import scala.util.{Failure, Try}

class FileIoError extends FileIoInterface {
	override def load(file: String): Try[BoardInterface] = new Failure[BoardInterface](new RuntimeException())

	override def save(file: String, boardInterface: BoardInterface): Try[Unit] = new Failure[Unit](new RuntimeException())
}
