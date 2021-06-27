package de.htwg.se.tiles.model.boardComponent

import de.htwg.se.tiles.model.SubPosition

import scala.collection.immutable.HashSet

case class Island(content: HashSet[SubPosition], complete: Boolean, value: Integer, owners: HashSet[String]) {
	require(content.nonEmpty)
}
