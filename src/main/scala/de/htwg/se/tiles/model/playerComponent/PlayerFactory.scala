package de.htwg.se.tiles.model.playerComponent

import scalafx.scene.paint.Color

trait PlayerFactory {
	def create(name: String, color: Color): PlayerInterface
}
