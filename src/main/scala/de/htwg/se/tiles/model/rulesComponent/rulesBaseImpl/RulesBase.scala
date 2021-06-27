package de.htwg.se.tiles.model.rulesComponent.rulesBaseImpl

import com.google.inject.Inject
import de.htwg.se.tiles.model.boardComponent.{BoardInterface, Island, TileInterface}
import de.htwg.se.tiles.model.rulesComponent.RulesInterface
import de.htwg.se.tiles.model.{Direction, Position, SubPosition}

import scala.collection.immutable.{HashMap, HashSet}

case class RulesBase(maxPeople: Int = 5, valueCenter: Double = 1.2, valueBorder: Double = 0.4) extends RulesInterface {
	@Inject
	def this() = this(5)

	override def canPlace(tile: TileInterface, tiles: HashMap[Position, TileInterface], at: Position): Boolean = {
		if (tiles.isEmpty) {
			return true
		}
		if (tiles.contains(at)) {
			return false
		}

		val check = at.neighbours()
			.filter(p => tiles.contains(p))
			.map(p => tiles(p).getTerrainAt(p.directionOfNeighbour(at).get).get == tile.getTerrainAt(at.directionOfNeighbour(p).get).get)
		check.forall(b => b) && check.nonEmpty

	}

	private def possiblePositions(b: BoardInterface): Set[Position] = b.tiles.keySet
		.flatMap(pos => pos.neighbours().filter(p => !b.tiles.contains(p)))

	private def makeFit(tile: TileInterface, b: BoardInterface, pos: Position): TileInterface = tile.set(
		north = b.tiles.get(pos.north()).fold(tile.north)(t => t.south),
		east = b.tiles.get(pos.east()).fold(tile.east)(t => t.west),
		south = b.tiles.get(pos.south()).fold(tile.south)(t => t.north),
		west = b.tiles.get(pos.west()).fold(tile.west)(t => t.east),
		center = tile.center
	)

	override def randomPlaceable(b: BoardInterface): TileInterface = if (b.tiles.isEmpty) b.getTileBuilder.randomTile() else b.getTileBuilder.rotateRandom(makeFit(b.getTileBuilder.randomTile(), b, possiblePositions(b).toVector(0))).get

	override def evaluatePoints(b: BoardInterface): BoardInterface = {
		val completeIslands = b.islands.filter(i => i.complete)
		val people = b.players.flatMap(p => p.people.map(pp => (p, pp)))
		var islandsWithPeople = Vector.empty[Island]
		people.foreach(p => {
			val pos = SubPosition.tupled(p._2)
			if (!islandsWithPeople.exists(i => i.content.contains(pos))) {
				islandsWithPeople = islandsWithPeople.appended(findIsland(b, pos))
			}
		})

		val islands = completeIslands.appendedAll(islandsWithPeople)
		val players = b.players
			.map(p => p.setPeople(p.people.filterNot(pp => islands.exists(i => i.content.contains(SubPosition.tupled(pp))))))
			.map(p => p.setPoints(islands.filter(i => i.complete && i.owners.contains(p.name)).map(i => i.value / i.owners.size).sum))

		b.create(islands = islands, players = players)
	}

	override def findIsland(b: BoardInterface, start: SubPosition): Island = {
		var content = HashSet(start)
		var queue = Vector(start)
		var complete = true
		while (queue.nonEmpty) {
			val currQueue = queue
			queue = Vector.empty
			currQueue.foreach(p => {
				val neighbours = p.neighbours().diff(content)

				val differentTile = neighbours.filter(n => n.position != p.position)

				val next = neighbours.filter(n =>
					b.tiles.get(n.position).map(t => t.getTerrainAt(n.direction)).exists(b.tiles.get(p.position).map(t => t.getTerrainAt(p.direction)).contains)
				)

				if (complete) {
					complete = differentTile.subsetOf(next)
				}

				queue = queue.appendedAll(next)
				content = content.union(next)
			})
		}
		val owners = HashSet.from(b.players.filter(p => p.people.exists(pp => content.contains(SubPosition.tupled(pp)))).map(p => p.name))
		val value = content.toVector.map(pp => if (pp.direction == Direction.Center) valueCenter else valueBorder).sum.intValue
		Island(content, complete, value, owners)
	}
}
