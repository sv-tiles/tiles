package de.htwg.se.tiles.model

import scala.collection.immutable.HashMap

case class Validator() {
	def canPlace(tile: Tile, tiles: HashMap[Position, Tile], at: Position): Boolean = {
		if (tiles.isEmpty) {
			return true
		}
		if (tiles.contains(at)) {
			return false
		}
		var c = 0;
		if (tiles.contains(at.west())) {
			c += 1
			if (tiles(at.west()).east != tile.west) {
				return false
			}
		}
		if (tiles.contains(at.north())) {
			c += 1
			if (tiles(at.north()).south != tile.north) {
				return false
			}
		}
		if (tiles.contains(at.east())) {
			c += 1
			if (tiles(at.east()).west != tile.east) {
				return false
			}
		}
		if (tiles.contains(at.south())) {
			c += 1
			if (tiles(at.south()).north != tile.south) {
				return false
			}
		}
		c > 0
	}

	@throws[IllegalArgumentException]("No tile to place")
	def canPlace(b: Board): Boolean = {
		if (b.currentPos.isEmpty) {
			throw new IllegalArgumentException("No tile to place")
		}
		canPlace(b.tiles(b.currentPos.get), b.pickupCurrentTile().tiles, b.currentPos.get)
	}

	private def possiblePositions(b: Board): Set[Position] = b.tiles.keySet
		.flatMap(pos => pos.neighbours().filter(p => !b.tiles.contains(p)))

	private def makeFit(tile: Tile, b: Board, pos: Position): Tile = tile
		.copy(north = b.tiles.get(pos.north()).fold(tile.north)(t => t.south))
		.copy(east = b.tiles.get(pos.east()).fold(tile.east)(t => t.west))
		.copy(south = b.tiles.get(pos.south()).fold(tile.south)(t => t.north))
		.copy(west = b.tiles.get(pos.west()).fold(tile.west)(t => t.east))

	def randomPlaceable(b: Board): Tile = if (b.tiles.isEmpty) Tile.random() else makeFit(Tile.random(), b, possiblePositions(b).toVector(0))
}
