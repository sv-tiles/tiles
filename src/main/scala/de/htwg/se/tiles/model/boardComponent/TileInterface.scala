package de.htwg.se.tiles.model.boardComponent

import de.htwg.se.tiles.model.Direction

import scala.util.Try

trait TileInterface {
	val north: Terrain
	val east: Terrain
	val south: Terrain
	val west: Terrain
	val center: Terrain

	def rotate(clockwise: Boolean = true): TileInterface

	def printLine(line: Int, width: Int, height: Int, border: Int, margin: Int): Try[String]

	def tileToString(width: Int, height: Int, border: Int, margin: Int): Try[String]

	def toString: String

	def getTerrainAt(direction: Direction): Try[Terrain]

	def set(north: Terrain = this.north, east: Terrain = this.east, south: Terrain = this.south, west: Terrain = this.west, center: Terrain = this.center): TileInterface
}
