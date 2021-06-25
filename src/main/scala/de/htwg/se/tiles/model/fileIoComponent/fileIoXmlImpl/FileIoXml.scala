package de.htwg.se.tiles.model.fileIoComponent.fileIoXmlImpl

import com.google.inject.{Guice, Inject, Injector}
import de.htwg.se.tiles.GameModule
import de.htwg.se.tiles.model.boardComponent._
import de.htwg.se.tiles.model.fileIoComponent.FileIoInterface
import de.htwg.se.tiles.model.fileIoComponent.fileIoXmlImpl.FileIoXml.{BoardInterfaceXML, ElemXML}
import de.htwg.se.tiles.model.playerComponent.{PlayerFactory, PlayerInterface}
import de.htwg.se.tiles.model.{Direction, Position}
import scalafx.scene.paint.Color

import scala.collection.immutable.HashMap
import scala.util.Try
import scala.xml.{Elem, Node, PrettyPrinter, XML}

case class FileIoXml @Inject()() extends FileIoInterface {
	val printer = new PrettyPrinter(120, 4, true)

	override def load(file: String): Try[BoardInterface] = XML.loadFile(file).toBoard

	override def save(file: String, boardInterface: BoardInterface): Try[Unit] = writeString(file, printer.format(boardInterface.toXml))

}

private object FileIoXml {
	implicit class BoardInterfaceXML(boardInterface: BoardInterface) {
		def toXml: Elem = <board>
			<current-player>
				{boardInterface.getCurrentPlayer.fold(0)(cp => boardInterface.players.indexOf(cp))}
			</current-player>
			<players>
				{boardInterface.players.map(p => p.toXml)}
			</players>
			<current-tile>
				{boardInterface.currentTile.map(t => t.toXml).getOrElse("")}
			</current-tile>
			<tile-map>
				{boardInterface.tiles.map(e => {
				<tile-map-item>
					{e._1.toXml}{e._2.toXml}
				</tile-map-item>
			})}
			</tile-map>
			<current-pos>
				{boardInterface.currentPos.map(p => p.toXml).getOrElse("")}
			</current-pos>
		</board>
	}

	implicit class TileInterfaceXML(tile: TileInterface) {
		def toXml: Elem = <tile>
			<north>
				{tile.north.getClass.getSimpleName}
			</north>
			<east>
				{tile.east.getClass.getSimpleName}
			</east>
			<west>
				{tile.west.getClass.getSimpleName}
			</west>
			<south>
				{tile.south.getClass.getSimpleName}
			</south>
			<center>
				{tile.center.getClass.getSimpleName}
			</center>
		</tile>
	}

	implicit class PlayerInterfaceXML(p: PlayerInterface) {
		def toXml: Elem = <player>
			<name>
				{p.name}
			</name>
			<color>
				<r>
					{p.color.red}
				</r>
				<g>
					{p.color.green}
				</g>
				<b>
					{p.color.blue}
				</b>
			</color>
			<points>
				{p.points}
			</points>
			<people>
				{p.people.map(pp => {
				<person>
					{pp._1.toXml}{pp._2.toXml}
				</person>
			})}
			</people>
		</player>
	}

	implicit class DirectionXML(direction: Direction) {
		def toXml: Elem = <direction>
			{direction.getClass.getSimpleName}
		</direction>
	}

	implicit class PositionXML(position: Position) {
		def toXml: Elem = <position>
			<x>
				{position.x}
			</x>
			<y>
				{position.y}
			</y>
		</position>
	}

	implicit class ElemXML(elem: Node) {
		private val injector: Injector = Guice.createInjector(GameModule())
		private val playerFactory: PlayerFactory = injector.getInstance(classOf[PlayerFactory])
		private val tileFactory: TileFactory = injector.getInstance(classOf[TileFactory])

		def toBoard: Try[BoardInterface] = Try {
			val currentPlayer = (elem \ "current-player").head.text.trim.toInt
			val players = (elem \ "players" \ "player").map(player => player.toPlayer.get).toVector
			val tiles = new HashMap[Position, TileInterface]().concat((elem \ "tile-map" \ "tile-map-item").map(n => ((n \ "position").head.toPosition.get, (n \ "tile").head.toTile.get)).toMap)

			val currentTileNode = (elem \ "current-tile").head
			val currentTile = if (currentTileNode.text.trim == "") Option.empty else Option((currentTileNode \ "tile").head.toTile.get)
			val currentPosNode = (elem \ "current-pos").head
			val currentPos = if (currentPosNode.text.trim == "") Option.empty else Option((currentPosNode \ "position").head.toPosition.get)

			injector.getInstance(classOf[BoardInterface]).create(players, currentPlayer, tiles, currentTile, currentPos)
		}

		def toPlayer: Try[PlayerInterface] = Try {
			val name = (elem \ "name").text.trim
			val colorR = (elem \ "color" \ "r").head.text.trim.toDouble
			val colorG = (elem \ "color" \ "g").head.text.trim.toDouble
			val colorB = (elem \ "color" \ "b").head.text.trim.toDouble
			val points = (elem \ "points").head.text.trim.toInt
			val people = (elem \ "people" \ "person").map(
				n => ((n \ "position").head.toPosition.get, stringToDirection((n \ "direction").head.text.trim).get)
			).toVector

			playerFactory.create(name, Color.color(colorR, colorG, colorB)).setPeople(people).setPoints(points)
		}

		def toTile: Try[TileInterface] = Try {
			val north = stringToTerrain((elem \ "north").head.text.trim).get
			val east = stringToTerrain((elem \ "east").head.text.trim).get
			val south = stringToTerrain((elem \ "south").head.text.trim).get
			val west = stringToTerrain((elem \ "west").head.text.trim).get
			val center = stringToTerrain((elem \ "center").head.text.trim).get

			tileFactory.create(north, east, south, west, center)
		}

		def toPosition: Try[Position] = Try {
			val x = (elem \ "x").head.text.trim.toInt
			val y = (elem \ "y").head.text.trim.toInt

			Position(x, y)
		}

		private def stringToTerrain(str: String): Try[Terrain] = Try(Terrain.defaults.find(t => t.getClass.getSimpleName == str).get)

		private def stringToDirection(str: String): Try[Direction] = Try(Direction.all.find(d => d.getClass.getSimpleName == str).get)
	}
}
