package de.htwg.se.tiles.model.boardComponent

import scala.util.Try

trait TileBuilderInterface {
	def randomTile(): TileInterface

	def rotateRandom(tile: TileInterface): Try[TileInterface]
}
