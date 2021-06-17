package de.htwg.se.tiles.model.rulesComponent.rulesBaseImpl

import com.google.inject.Inject
import de.htwg.se.tiles.model.Position
import de.htwg.se.tiles.model.boardComponent.{BoardInterface, TileInterface}
import de.htwg.se.tiles.model.rulesComponent.RulesInterface

import scala.collection.immutable.HashMap

case class RulesBase(maxPeople: Int = 5) extends RulesInterface {
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
		// TODO people -> points, findIslands
		b
	}
}
