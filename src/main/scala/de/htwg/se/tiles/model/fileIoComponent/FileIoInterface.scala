package de.htwg.se.tiles.model.fileIoComponent

import de.htwg.se.tiles.model.boardComponent.BoardInterface

import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import scala.util.{Try, Using}

trait FileIoInterface {
	def load(file: String): Try[BoardInterface]

	def save(file: String, boardInterface: BoardInterface): Try[Unit]

	def writeString(file: String, text: String): Try[Unit] = Using(new FileOutputStream(file)) { fos => fos.write((text + '\n').getBytes(StandardCharsets.UTF_8)) }
}
