package de.htwg.se.tiles.util

case class Position2D(x: Double, y: Double) {
	def +(p: Position2D): Position2D = Position2D(x + p.x, y + p.y)

	def -(p: Position2D): Position2D = Position2D(x - p.x, y - p.y)

	def +(x2: Double, y2: Double): Position2D = Position2D(x + x2, y + y2)

	def -(x2: Double, y2: Double): Position2D = Position2D(x - x2, y - y2)
}
