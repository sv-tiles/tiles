package de.htwg.se.tiles.model

import scala.collection.immutable.Set

case class SubPosition(position: Position, direction: Direction) {
	def east(): SubPosition = direction match {
		case Direction.East => SubPosition(position.east(), Direction.West)
		case Direction.West => SubPosition(position, Direction.Center)
		case _ => SubPosition(position, Direction.East)
	}

	def west(): SubPosition = direction match {
		case Direction.West => SubPosition(position.west(), Direction.East)
		case Direction.East => SubPosition(position, Direction.Center)
		case _ => SubPosition(position, Direction.West)
	}

	def north(): SubPosition = direction match {
		case Direction.North => SubPosition(position.north(), Direction.South)
		case Direction.South => SubPosition(position, Direction.Center)
		case _ => SubPosition(position, Direction.North)
	}

	def south(): SubPosition = direction match {
		case Direction.South => SubPosition(position.south(), Direction.North)
		case Direction.North => SubPosition(position, Direction.Center)
		case _ => SubPosition(position, Direction.South)
	}

	def neighbours(): Set[SubPosition] = Set(north(), east(), south(), west())
}
