package de.htwg.se.tiles.model

import scala.collection.immutable.Set

case class Position(x: Int, y: Int) {
	def east(): Position = copy(x = x + 1)

	def west(): Position = copy(x = x - 1)

	def north(): Position = copy(y = y - 1)

	def south(): Position = copy(y = y + 1)

	def neighbours(): Set[Position] = Set(north(), east(), south(), west())
}
