package de.htwg.se.tiles.model.rulesComponent.rulesFakeImpl

import de.htwg.se.tiles.model.Position
import de.htwg.se.tiles.model.boardComponent.{BoardInterface, TileInterface}
import de.htwg.se.tiles.model.rulesComponent.RulesInterface

import scala.collection.immutable.HashMap

case class RulesFake() extends RulesInterface {
	override def canPlace(tile: TileInterface, tiles: HashMap[Position, TileInterface], at: Position): Boolean = !tiles.contains(at)

	override def randomPlaceable(b: BoardInterface): TileInterface = b.getTileBuilder.randomTile()
}
