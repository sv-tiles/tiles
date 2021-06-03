package de.htwg.se.tiles.model.rules

import de.htwg.se.tiles.model.{Board, Position, Tile, TileBuilder}

import scala.collection.immutable.HashMap

case class BasicRules() extends Rules {
	override def canPlace(tile: Tile, tiles: HashMap[Position, Tile], at: Position): Boolean = {
		if (tiles.isEmpty) {
			return true
		}
		if (tiles.contains(at)) {
			return false
		}

		val check = at.neighbours()
			.filter(p => tiles.contains(p))
			.map(p => tiles(p).getTerrainAt(p.directionOfNeighbour(at).get).get == tile.getTerrainAt(at.directionOfNeighbour(p).get).get)
		check.forall(b => b) && check.nonEmpty
		/*
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
		*/
	}

	private def possiblePositions(b: Board): Set[Position] = b.tiles.keySet
		.flatMap(pos => pos.neighbours().filter(p => !b.tiles.contains(p)))

	private def makeFit(tile: Tile, b: Board, pos: Position): Tile = tile
		.copy(north = b.tiles.get(pos.north()).fold(tile.north)(t => t.south))
		.copy(east = b.tiles.get(pos.east()).fold(tile.east)(t => t.west))
		.copy(south = b.tiles.get(pos.south()).fold(tile.south)(t => t.north))
		.copy(west = b.tiles.get(pos.west()).fold(tile.west)(t => t.east))

	override def randomPlaceable(b: Board): Tile = if (b.tiles.isEmpty) TileBuilder.randomTile() else TileBuilder.rotateRandom(makeFit(TileBuilder.randomTile(), b, possiblePositions(b).toVector(0))).get
}
