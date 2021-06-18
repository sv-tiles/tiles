package de.htwg.se.tiles.view.gui

import de.htwg.se.tiles.control.controllerComponent.ControllerInterface
import de.htwg.se.tiles.model.Position
import de.htwg.se.tiles.model.boardComponent.{Terrain, TileInterface}
import de.htwg.se.tiles.util.{Observer, Position2D}
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.control._
import scalafx.scene.input.MouseButton
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.shape.{Polygon, Polyline, Rectangle, Shape}
import scalafx.scene.text.Text
import scalafx.scene.{Group, Scene}


class Gui(val controller: ControllerInterface) extends JFXApp3 with Observer[(Boolean, String)] {
	private var size = 50d
	private var offset = Position2D(0d, 0d)

	private val pane: Pane = new Pane {
		style = "-fx-background-color: rgb(50,50,50)"
	}

	private val info = new Text("") {
		style = "-fx-fill: white; -fx-font-size: 20px"
	}
	private var infoTime = 0L

	private val player = new Text("no players") {
		style = "-fx-fill: white; -fx-font-size: 20px"
	}

	override def start(): Unit = {
		stage = new JFXApp3.PrimaryStage {
			title.value = "Tiles"
			width = 800
			height = 500
			scene = new Scene {
				stylesheets.add(getClass.getResource("style.css").toString)
				root = new BorderPane {
					style = "-fx-background-color: rgb(100,100,100)"
					center = pane
					top = new MenuBar {
						menus = List(
							new Menu("Game") {
								items = List(
									new MenuItem("Clear") {
										onAction = e => controller.clear()
									},
									new MenuItem("Add player") {
										onAction = _ => new TextInputDialog() {
											initOwner(stage)
											title = "Add player"
											contentText = "name:"
										}.showAndWait().foreach(name => {
											controller.addPlayer(name)
											update()
										})
									}
								)
							}
						)
					}
					bottom = new HBox(10) {
						children = List(player, info)
					}
					left = new VBox {
						children = List(
							new Button("undo") {
								onAction = e => controller.undo()
							},
							new Button("redo") {
								onAction = e => controller.redo()
							}
						)
					}
				}
				private var anchor = Position2D(0d, 0d)
				onMousePressed = event => if (event.getButton == MouseButton.Secondary.delegate) {
					anchor = Position2D(event.getSceneX, event.getSceneY)
				}
				onMouseDragged = event => if (event.getButton == MouseButton.Secondary.delegate) {
					offset += anchor - (event.getSceneX, event.getSceneY)
					anchor = Position2D(event.getSceneX, event.getSceneY)
					update()
				}
				onScroll = e => {
					size = (1d - e.getDeltaY / e.getMultiplierY / 10d) * size
					update()
				}
			}
		}

		stage.width.onChange { (_, _, _) => update() }
		stage.height.onChange { (_, _, _) => update() }

		controller.add(this)
		update()
	}

	def boardToLocal(pos: Position): Position2D =
		Position2D(pos.x * size + pane.width.value / 2d - size / 2d, pos.y * size + pane.height.value / 2d - size / 2d) - offset

	def localToBoard(pos: Position2D): (Int, Int) =
		(((pos.x + offset.x + size / 2d - pane.width.value / 2d) / size).round.intValue, ((pos.y + offset.y + size / 2d - pane.height.value / 2d) / size).round.intValue)

	override def update(value: (Boolean, String) = (true, "")): Unit = {
		if (controller.board.players.isEmpty) {
			info.text = "No players!"
			return
		}

		player.text = "Player: " + controller.board.getCurrentPlayer.name

		if (value._2.nonEmpty) {
			info.text = value._2
			infoTime = System.currentTimeMillis()
		} else if (System.currentTimeMillis() - infoTime > 1000) {
			info.text = "-"
		}
		Platform.runLater(() => {
			var moveable = Option.empty[Group]
			pane.children = controller.board.tiles.map(t => new Group {
				private val (pos, tile) = t
				private val screenPos = boardToLocal(pos)
				children = generatePolygons(tile, screenPos, size)
				if (controller.board.currentPos.contains(pos)) {
					children.add(new Polyline {
						points.addAll(
							screenPos.x,
							screenPos.y,
							screenPos.x + size,
							screenPos.y,
							screenPos.x + size,
							screenPos.y + size,
							screenPos.x,
							screenPos.y + size,
							screenPos.x,
							screenPos.y,
						)
						stroke = Color.color(1, 0, 0)
					})
					var anchor = Position2D(0, 0)
					onMousePressed = e => if (e.isPrimaryButtonDown) {
						anchor = Position2D(e.getSceneX - translateX.value, e.getSceneY - translateY.value)
						println(anchor)
					} else if (e.isForwardButtonDown) {
						controller.rotate(false)
					} else if (e.isBackButtonDown) {
						controller.rotate(true)
					} else if (e.isMiddleButtonDown) {
						// TODO place people
						controller.commit(Option.empty)
					}
					onMouseDragged = e => if (e.isPrimaryButtonDown) {
						translateX.value = e.getSceneX - anchor.x
						translateY.value = e.getSceneY - anchor.y
					}
					onMouseReleased = e => if (!e.isPrimaryButtonDown) {
						val board = controller.board
						board.currentPos.foreach(p => {
							controller.placeTile(p.x + (translateX.value / size).round.intValue, p.y + (translateY.value / size).round.intValue)

							if (controller.board.currentPos.isEmpty) {
								controller.placeTile(p.x, p.y)
							}
						})

						update()
					}
					moveable = Option(this)
				}
			})
			moveable.foreach(e => {
				pane.children.remove(e)
				pane.children.add(e)
			})
			if (controller.board.currentTile.isEmpty) {
				pane.children.add(new Button {
					translateX.value = pane.width.value - 50
					maxWidth(size)
					minWidth(size)
					maxHeight(size)
					minHeight(size)
					text = "Reset"
					onAction = _ => controller.pickUpTile()
				})
			}
			controller.board.currentTile.foreach(tile => pane.children.add(new Group {
				children = generatePolygons(tile, Position2D(pane.width.value - size, 0), size)
				var anchor = Position2D(0, 0)
				var off = Position2D(0, 0)
				onMousePressed = e => if (e.isPrimaryButtonDown) {
					anchor = Position2D(e.getSceneX, e.getSceneY)
					val tmp = pane.sceneToLocal(e.getSceneX, e.getSceneY)
					off = Position2D(tmp.x - (pane.width.value - size), tmp.y - 0)
				}
				onMouseDragged = e => if (e.isPrimaryButtonDown) {
					translateX.value = e.getSceneX - anchor.x
					translateY.value = e.getSceneY - anchor.y
				}
				onMouseReleased = e => if (!e.isPrimaryButtonDown) {
					val tmp2 = pane.sceneToLocal(e.getSceneX, e.getSceneY)
					val tmp = localToBoard(Position2D(tmp2.x - off.x, tmp2.y - off.y))
					controller.placeTile(tmp._1, tmp._2)
					if (controller.board.currentTile.isDefined) {
						translateX.value = 0
						translateY.value = 0
					}
				}
			}))
		})
	}

	def colorOf(terrain: Terrain): Color = terrain match {
		case Terrain.Hills => Color.color(80 / 255d, 65 / 255d, 0)
		case Terrain.Water => Color.color(0, 180 / 255d, 230 / 255d)
		case Terrain.Forest => Color.color(0, 160 / 255d, 0)
		case Terrain.Mountains => Color.color(160 / 255d, 160 / 255d, 160 / 255d)
		case Terrain.Plains => Color.color(90 / 255d, 150 / 255d, 50 / 255d)
		case _ => Color.color(0, 0, 1)
	}

	def generatePolygons(tile: TileInterface, pos: Position2D, size: Double): List[Shape] = List(
		new Polygon {
			points.addAll(
				pos.x,
				pos.y,
				pos.x + size * .2,
				pos.y + size * .2,
				pos.x + size * .8,
				pos.y + size * .2,
				pos.x + size,
				pos.y
			)
			fill = colorOf(tile.north)
		},
		new Polygon {
			points.addAll(
				pos.x + size,
				pos.y,
				pos.x + size * .8,
				pos.y + size * .2,
				pos.x + size * .8,
				pos.y + size * .8,
				pos.x + size,
				pos.y + size
			)
			fill = colorOf(tile.east)
		},
		new Polygon {
			points.addAll(
				pos.x,
				pos.y + size,
				pos.x + size * .2,
				pos.y + size * .8,
				pos.x + size * .8,
				pos.y + size * .8,
				pos.x + size,
				pos.y + size
			)
			fill = colorOf(tile.south)
		},
		new Polygon {
			points.addAll(
				pos.x,
				pos.y,
				pos.x + size * .2,
				pos.y + size * .2,
				pos.x + size * .2,
				pos.y + size * .8,
				pos.x,
				pos.y + size
			)
			fill = colorOf(tile.west)
		},
		new Rectangle {
			x = pos.x +.2 * size
			y = pos.y +.2 * size
			width = size * .6
			height = size * .6
			fill = colorOf(tile.center)
		}
	)
}
