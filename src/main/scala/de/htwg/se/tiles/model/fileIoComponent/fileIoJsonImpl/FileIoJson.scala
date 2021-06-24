package de.htwg.se.tiles.model.fileIoComponent.fileIoJsonImpl

import com.google.inject.Inject
import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.model.fileIoComponent.FileIoInterface

import scala.util.Try

case class FileIoJson @Inject()() extends FileIoInterface {
	override def load(file: String): Try[BoardInterface] = ???

	override def save(file: String, boardInterface: BoardInterface): Try[Unit] = ???
}
