package de.htwg.se.tiles.model.boardComponent

import com.google.inject.assistedinject.Assisted

trait TileFactory {
	def create(@Assisted("north") north: Terrain, @Assisted("east") east: Terrain, @Assisted("south") south: Terrain, @Assisted("west") west: Terrain, @Assisted("center") center: Terrain): TileInterface
}
