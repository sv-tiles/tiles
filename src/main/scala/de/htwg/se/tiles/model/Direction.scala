package de.htwg.se.tiles.model

sealed trait Direction

object Direction {
	case object North extends Direction

	case object East extends Direction

	case object South extends Direction

	case object West extends Direction

	val all: Iterable[Direction] = List(North, East, South, West)
}