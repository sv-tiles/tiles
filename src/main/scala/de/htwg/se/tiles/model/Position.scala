package de.htwg.se.tiles.model

import scala.collection.immutable.Set
import scala.util.{Failure, Success, Try}

case class Position(x: Int, y: Int) {
	def east(): Position = copy(x = x + 1)

	def west(): Position = copy(x = x - 1)

	def north(): Position = copy(y = y - 1)

	def south(): Position = copy(y = y + 1)

	def neighbour(direction: Direction): Position = direction match {
		case Direction.North => north()
		case Direction.East => east()
		case Direction.South => south()
		case Direction.West => west()
		case Direction.Center => this
	}

	def neighbours(): Set[Position] = Set(north(), east(), south(), west())

	def directionOfNeighbour(position: Position): Try[Direction] =
		Direction.allDirections.find(d => neighbour(d) == position).fold[Try[Direction]](Failure(new IllegalArgumentException()))(d => Success(d))
}
