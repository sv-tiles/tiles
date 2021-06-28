package de.htwg.se.tiles.model.playerComponent.playerBaseImpl

import com.google.inject.Inject
import com.google.inject.assistedinject.Assisted
import de.htwg.se.tiles.model.playerComponent.PlayerInterface
import de.htwg.se.tiles.model.{Direction, Position}
import scalafx.scene.paint.Color

case class PlayerBase(name: String, color: Color, people: Vector[(Position, Direction)] = Vector.empty, points: Int = 0) extends PlayerInterface {
	@Inject
	def this(@Assisted name: String, @Assisted color: Color) = this(name, color, people = Vector.empty, points = 0)

	override def setPeople(people: Vector[(Position, Direction)]): PlayerInterface = copy(people = people)

	override def setPoints(points: Int): PlayerInterface = copy(points = points)

	override def setColor(color: Color): PlayerInterface = copy(color = color)
}
