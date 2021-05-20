package de.htwg.se.tiles.model

import de.htwg.se.tiles.model.Direction.{East, North, South, West}

import scala.collection.immutable.Set
import scala.util.{Failure, Success, Try}

case class Position(x: Int, y: Int) {
	def east(): Position = copy(x = x + 1)

	def west(): Position = copy(x = x - 1)

	def north(): Position = copy(y = y - 1)

	def south(): Position = copy(y = y + 1)

	def neighbour(direction: Direction): Position = direction match {
		case North => north()
		case East => east()
		case South => south()
		case West => west()
	}

	def neighbours(): Set[Position] = Set(north(), east(), south(), west())

	def directionOfNeighbour(position: Position): Try[Direction] =
		Direction.all.find(d => neighbour(d) == position).fold[Try[Direction]](Failure(new IllegalArgumentException()))(d => Success(d))
}
