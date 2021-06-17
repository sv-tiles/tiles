package de.htwg.se.tiles.model.playerComponent

import com.google.inject.assistedinject.Assisted
import scalafx.scene.paint.Color

trait PlayerFactory {
	def create(@Assisted("name") name: String, @Assisted("color") color: Color): PlayerInterface;
}
