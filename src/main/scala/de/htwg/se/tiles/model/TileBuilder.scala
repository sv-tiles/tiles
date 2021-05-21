package de.htwg.se.tiles.model

import scala.util.{Failure, Random, Try}

object TileBuilder {
	private var rand = (max: Int) => Random.nextInt(max)

	private def setRandom(random: Int => Int): Unit = rand = random

	def randomTile(): Tile = {
		val arr = (Vector() ++ Terrain.defaults).sortBy(e => e.hashCode())
		val max = arr.size
		Tile(arr(rand(max)), arr(rand(max)), arr(rand(max)), arr(rand(max)), arr(rand(max)))
	}

	def rotateRandom(tile: Tile, rotations: Int = rand(4)): Try[Tile] =
		rotations match {
			case 0 => Try(tile)
			case 1 => Try(tile.rotate(true))
			case 2 => Try(tile.rotate(true).rotate(true))
			case 3 => Try(tile.rotate(false))
			case _ => Failure(new IllegalArgumentException())
		}
}
