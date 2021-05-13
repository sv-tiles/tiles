package de.htwg.se.tiles.model

import scala.util.Random

object TileBuilder {
	private var rand = (max: Int) => Random.nextInt(max)

	private def setRandom(random: Int => Int): Unit = rand = random

	def randomTile(): Tile = {
		val arr = (Vector() ++ Terrain.defaults).sortBy(e => e.hashCode())
		val max = arr.size
		Tile(arr(rand(max)), arr(rand(max)), arr(rand(max)), arr(rand(max)), arr(rand(max)))
	}

	def rotateRandom(tile: Tile, rotations: Int = rand(4)): Tile =
		rotations match {
			case 0 => tile
			case 1 => tile.rotate(true)
			case 2 => tile.rotate(true).rotate(true)
			case 3 => tile.rotate(false)
			case _ => throw new IllegalArgumentException()
		}
}
