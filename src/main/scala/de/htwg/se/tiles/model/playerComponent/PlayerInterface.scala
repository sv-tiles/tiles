package de.htwg.se.tiles.model.playerComponent

import de.htwg.se.tiles.model.{Direction, Position}
import scalafx.scene.paint.Color

trait PlayerInterface {
	def name: String

	def color: Color

	def setColor(color: Color): PlayerInterface

	def people: Vector[(Position, Direction)]

	def setPeople(people: Vector[(Position, Direction)]): PlayerInterface

	def points: Int

	def setPoints(points: Int): PlayerInterface
}
