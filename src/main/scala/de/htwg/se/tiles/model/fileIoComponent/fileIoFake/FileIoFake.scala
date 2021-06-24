package de.htwg.se.tiles.model.fileIoComponent.fileIoFake

import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.model.fileIoComponent.FileIoInterface

import java.io.File
import scala.collection.immutable.HashMap
import scala.util.Try

class FileIoFake extends FileIoInterface {
	private var inMemoryStorage = new HashMap[String, BoardInterface]()

	override def load(file: String): Try[BoardInterface] = Try(inMemoryStorage(key(file)))

	override def save(file: String, boardInterface: BoardInterface): Try[Unit] = Try {
		inMemoryStorage = inMemoryStorage.updated(key(file), boardInterface)
	}

	private def key(file: String): String = {
		new File(file).getAbsolutePath
	}
}
