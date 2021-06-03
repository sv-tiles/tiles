package de.htwg.se.tiles.model.rules

import de.htwg.se.tiles.model.{Board, Position, Tile, TileBuilder}

import scala.collection.immutable.HashMap

case class NoRules() extends Rules {
	override def canPlace(tile: Tile, tiles: HashMap[Position, Tile], at: Position): Boolean = !tiles.contains(at)

	override def randomPlaceable(b: Board): Tile = TileBuilder.randomTile()
}
