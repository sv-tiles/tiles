package de.htwg.se.tiles.model.boardComponent.boardBaseImpl

import de.htwg.se.tiles.model.boardComponent.{Terrain, TileBuilderInterface, TileInterface}

import scala.util.{Failure, Random, Try}

object TileBuilder extends TileBuilderInterface {
	private var rand = (max: Int) => Random.nextInt(max)

	private def setRandom(random: Int => Int): Unit = rand = random

	override def randomTile(): Tile = {
		val arr = (Vector() ++ Terrain.defaults).sortBy(e => e.hashCode())
		val max = arr.size
		Tile(arr(rand(max)), arr(rand(max)), arr(rand(max)), arr(rand(max)), arr(rand(max)))
	}

	override def rotateRandom(tile: TileInterface): Try[TileInterface] = rotateRandom(tile, rand(4))

	def rotateRandom(tile: TileInterface, rotations: Int = rand(4)): Try[TileInterface] =
		rotations match {
			case 0 => Try(tile)
			case 1 => Try(tile.rotate(true))
			case 2 => Try(tile.rotate(true).rotate(true))
			case 3 => Try(tile.rotate(false))
			case _ => Failure(new IllegalArgumentException())
		}
}
