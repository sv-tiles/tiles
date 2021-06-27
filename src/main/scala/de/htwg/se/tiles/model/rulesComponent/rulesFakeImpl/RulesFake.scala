package de.htwg.se.tiles.model.rulesComponent.rulesFakeImpl

import com.google.inject.Inject
import de.htwg.se.tiles.model.boardComponent.{BoardInterface, Island, TileInterface}
import de.htwg.se.tiles.model.rulesComponent.RulesInterface
import de.htwg.se.tiles.model.{Direction, Position, SubPosition}

import scala.collection.immutable.{HashMap, HashSet}

case class RulesFake(maxPeople: Int = 5) extends RulesInterface {
	@Inject
	def this() = this(5)

	override def canPlace(tile: TileInterface, tiles: HashMap[Position, TileInterface], at: Position): Boolean = !tiles.contains(at)

	override def randomPlaceable(b: BoardInterface): TileInterface = b.getTileBuilder.randomTile()

	override def assignPoints(b: BoardInterface): BoardInterface = {
		val players = b.players
			.map(p => p.setPeople(p.people.filterNot(pp => b.islands.exists(i => i.content.contains(SubPosition.tupled(pp))))))
			.map(p => p.setPoints(b.islands.filter(i => i.owners.contains(p.name)).map(i => i.value / i.owners.size).sum))

		b.create(players = players)
	}

	override def findIsland(b: BoardInterface, start: SubPosition): Island = {
		val center = SubPosition(start.position, Direction.Center)
		val content = HashSet(center).union(center.neighbours())
		val owners = HashSet.from(b.players.filter(p => p.people.exists(pp => content.contains(SubPosition.tupled(pp)))).map(p => p.name))
		Island(content, complete = true, 2, owners)
	}
}
