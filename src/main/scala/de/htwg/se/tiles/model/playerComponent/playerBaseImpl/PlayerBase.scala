package de.htwg.se.tiles.model.playerComponent.playerBaseImpl

import de.htwg.se.tiles.model.playerComponent.PlayerInterface
import de.htwg.se.tiles.model.{Direction, Position}
import scalafx.scene.paint.Color

case class PlayerBase(name: String, color: Color, people: Vector[(Position, Direction)] = Vector.empty, points: Int = 0) extends PlayerInterface {
	override def setPeople(people: Vector[(Position, Direction)]): PlayerInterface = copy(people = people)

	override def setPoints(points: Int): PlayerInterface = copy(points = points)
}
