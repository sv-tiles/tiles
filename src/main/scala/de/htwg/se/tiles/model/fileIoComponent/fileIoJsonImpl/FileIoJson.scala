package de.htwg.se.tiles.model.fileIoComponent.fileIoJsonImpl

import com.google.inject.{Guice, Inject, Injector}
import de.htwg.se.tiles.GameModule
import de.htwg.se.tiles.model.boardComponent._
import de.htwg.se.tiles.model.fileIoComponent.FileIoInterface
import de.htwg.se.tiles.model.fileIoComponent.fileIoJsonImpl.FileIoJson.{BoardInterfaceJson, JsValueJson}
import de.htwg.se.tiles.model.playerComponent.{PlayerFactory, PlayerInterface}
import de.htwg.se.tiles.model.{Direction, Position, SubPosition}
import play.api.libs.json.{JsObject, JsValue, Json}
import scalafx.scene.paint.Color

import scala.collection.immutable.{HashMap, HashSet}
import scala.util.Try

case class FileIoJson @Inject()() extends FileIoInterface {
	override def load(file: String): Try[BoardInterface] = readString(file).flatMap(str => Json.parse(str).toBoard)

	override def save(file: String, boardInterface: BoardInterface): Try[Unit] = writeString(file, boardInterface.toJson.toString())
}

private object FileIoJson {
	implicit class BoardInterfaceJson(boardInterface: BoardInterface) {
		def toJson: JsObject = Json.obj(
			"currentPlayer" -> boardInterface.currentPlayer,
			"players" -> boardInterface.players.map(p => p.toJson),
			"currentTile" -> boardInterface.currentTile.map[Json.JsValueWrapper](t => t.toJson).getOrElse(""),
			"tileMap" -> boardInterface.tiles.map(e => Json.obj("position" -> e._1.toJson, "tile" -> e._2.toJson)),
			"currentPos" -> boardInterface.currentPos.map[Json.JsValueWrapper](p => p.toJson).getOrElse(""),
			"islands" -> boardInterface.islands.map(i => i.toJson)
		)
	}

	implicit class TileInterfaceJson(tileInterface: TileInterface) {
		def toJson: JsObject = Json.obj(
			"north" -> tileInterface.north.getClass.getSimpleName,
			"east" -> tileInterface.east.getClass.getSimpleName,
			"south" -> tileInterface.south.getClass.getSimpleName,
			"west" -> tileInterface.west.getClass.getSimpleName,
			"center" -> tileInterface.center.getClass.getSimpleName
		)
	}

	implicit class PlayerInterfaceJson(playerInterface: PlayerInterface) {
		def toJson: JsObject = Json.obj(
			"name" -> playerInterface.name,
			"color" -> Json.obj(
				"r" -> playerInterface.color.red,
				"g" -> playerInterface.color.green,
				"b" -> playerInterface.color.blue
			),
			"points" -> playerInterface.points,
			"people" -> playerInterface.people.map(pp => Json.obj(
				"position" -> pp._1.toJson,
				"direction" -> pp._2.toJson
			)
			)
		)
	}

	implicit class DirectionJson(direction: Direction) {
		def toJson: String = direction.getClass.getSimpleName
	}

	implicit class PositionJson(position: Position) {
		def toJson: JsObject = Json.obj(
			"x" -> position.x,
			"y" -> position.y
		)
	}

	implicit class SubPositionJson(subPosition: SubPosition) {
		def toJson: JsObject = Json.obj(
			"position" -> subPosition.position.toJson,
			"direction" -> subPosition.direction.toJson
		)
	}

	implicit class IslandJson(island: Island) {
		def toJson: JsObject = Json.obj(
			"content" -> island.content.map(c => c.toJson),
			"complete" -> island.complete,
			"value" -> island.value.intValue,
			"owners" -> island.owners
		)
	}

	implicit class JsValueJson(json: JsValue) {
		private val injector: Injector = Guice.createInjector(GameModule())
		private val playerFactory: PlayerFactory = injector.getInstance(classOf[PlayerFactory])
		private val tileFactory: TileFactory = injector.getInstance(classOf[TileFactory])

		def toBoard: Try[BoardInterface] = Try {
			val currentPlayer = (json \ "currentPlayer").get.toString().toInt
			val players = (json \ "players").as[List[JsValue]].map(player => player.toPlayer.get).toVector
			val tiles = new HashMap[Position, TileInterface]().concat((json \ "tileMap").as[List[JsValue]].map(n => ((n \ "position").get.toPosition.get, (n \ "tile").get.toTile.get)).toMap)

			val currentTileNode = (json \ "currentTile").get
			val currentTile = if (currentTileNode.toString() == "\"\"") Option.empty else Option(currentTileNode.toTile.get)
			val currentPosNode = (json \ "currentPos").get
			val currentPos = if (currentPosNode.toString() == "\"\"") Option.empty else Option(currentPosNode.toPosition.get)
			val islands = (json \ "islands").as[List[JsValue]].map(island => island.toIsland.get).toVector

			injector.getInstance(classOf[BoardInterface]).create(players, currentPlayer, tiles, currentTile, currentPos, islands)
		}

		def toPlayer: Try[PlayerInterface] = Try {
			val name = (json \ "name").get.as[String]
			val colorR = (json \ "color" \ "r").get.toString().toDouble
			val colorG = (json \ "color" \ "g").get.toString().toDouble
			val colorB = (json \ "color" \ "b").get.toString().toDouble
			val points = (json \ "points").get.toString().toInt
			val people = (json \ "people").as[List[JsValue]].map(
				n => ((n \ "position").get.toPosition.get, stringToDirection((n \ "direction").get.as[String]).get)
			).toVector

			playerFactory.create(name, Color.color(colorR, colorG, colorB)).setPeople(people).setPoints(points)
		}

		def toTile: Try[TileInterface] = Try {
			val north = stringToTerrain((json \ "north").get.as[String]).get
			val east = stringToTerrain((json \ "east").get.as[String]).get
			val south = stringToTerrain((json \ "south").get.as[String]).get
			val west = stringToTerrain((json \ "west").get.as[String]).get
			val center = stringToTerrain((json \ "center").get.as[String]).get

			tileFactory.create(north, east, south, west, center)
		}

		def toPosition: Try[Position] = Try {
			val x = (json \ "x").get.toString().toInt
			val y = (json \ "y").get.toString().toInt

			Position(x, y)
		}

		def toSubPosition: Try[SubPosition] = Try {
			val position = (json \ "position").get.toPosition.get
			val direction = stringToDirection((json \ "direction").get.as[String]).get

			SubPosition(position, direction)
		}

		def toIsland: Try[Island] = Try {
			val content = HashSet.from((json \ "content").as[List[JsValue]].map(v => v.toSubPosition.get))
			val complete = (json \ "complete").as[Boolean]
			val value = (json \ "value").as[Int]
			val owners = HashSet.from((json \ "owners").as[List[String]])
			Island(content, complete, value, owners)
		}

		private def stringToTerrain(str: String): Try[Terrain] = Try(Terrain.defaults.find(t => t.getClass.getSimpleName == str).get)

		private def stringToDirection(str: String): Try[Direction] = Try(Direction.all.find(d => d.getClass.getSimpleName == str).get)
	}
}
