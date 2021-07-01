package de.htwg.se.tiles.view.gui

import de.htwg.se.tiles.control.controllerComponent.ControllerInterface
import de.htwg.se.tiles.model.boardComponent.{Terrain, TileInterface}
import de.htwg.se.tiles.model.{Direction, Position, SubPosition}
import de.htwg.se.tiles.util.{Observer, Position2D}
import scalafx.application.{JFXApp3, Platform}
import scalafx.geometry.Insets
import scalafx.scene.control.Alert.AlertType
import scalafx.scene.control.ScrollPane.ScrollBarPolicy
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.shape._
import scalafx.scene.text.Text
import scalafx.scene.{Group, Node, Scene}
import scalafx.stage.FileChooser


class Gui(val controller: ControllerInterface) extends JFXApp3 with Observer[(Boolean, String)] {
	private var size = 100d
	private var offset = Position2D(0d, 0d)

	private var placePeople: Option[SubPosition] = Option.empty
	private var placePeopleNode: Node = new Circle() {
		visible = false
	}

	private var highlight: Option[SubPosition] = Option.empty
	private var highlightGroup: Group = new Group() {
		mouseTransparent = true
	}

	private var anchor = Position2D(0, 0)
	private var lastClick: Long = 0

	private val pane: Pane = new Pane {
		style = "-fx-background-color: rgb(50,50,50)"
		val rect: Rectangle = Rectangle(0, 0)
		rect.widthProperty().bind(this.widthProperty())
		rect.heightProperty().bind(this.heightProperty())
		this.setClip(rect)
	}

	private val players = new VBox() {
		prefWidth = 200
		style = "-fx-background-color: rgb(100,100,100);"
	}

	private val info = new Text("") {
		style = "-fx-fill: white; -fx-font-size: 20px"
	}
	private var infoTime = 0L

	override def start(): Unit = {
		stage = new JFXApp3.PrimaryStage {
			title.value = "Tiles"
			width = 1000
			height = 800
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
									new MenuItem("Save") {
										onAction = _ => {
											val file = new FileChooser().showSaveDialog(stage)
											if (file != null) {
												controller.save(file.getAbsolutePath)
											}
										}
									},
									new MenuItem("Load") {
										onAction = _ => {
											val file = new FileChooser().showOpenDialog(stage)
											if (file != null) {
												controller.load(file.getAbsolutePath)
											}
										}
									},
									new SeparatorMenuItem(),
									new MenuItem("Exit") {
										onAction = _ => stage.close()
									}
								)
							},
							new Menu("View") {
								items = List(
									new MenuItem("Toggle Fullscreen") {
										onAction = _ => stage.setFullScreen(!stage.isFullScreen)
									}
								)
							},
							new Menu("Help") {
								items = List(
									new MenuItem("Controls") {
										onAction = _ => new Alert(AlertType.Information) {
											title = "Controls"
											headerText = "Controls"
											initOwner(stage)
											contentText = "Everywhere:\n" +
												"Mouse Wheel: Change zoom\n" +
												"Secondary Mouse Button: Move view\n" +
												"\n" +
												"On active tile:\n" +
												"Primary Mouse Button: Drag tiles, double click to place people\n" +
												"Middle Mouse Button: End turn\n" +
												"Back Mouse Button: Rotate tile clockwise\n" +
												"Forward Mouse Button: Rotate tile counterclockwise"
											dialogPane.value.setMinWidth(600)
										}.show()
									},
									new MenuItem("Points") {
										onAction = _ => new Alert(AlertType.Information) {
											title = "Points"
											headerText = "Points"
											initOwner(stage)
											contentText = "1.2 points per center region\n" +
												"0.4 points per border region"
										}.show()
									}
								)
							}
						)
					}
					bottom = info
					left = new VBox {
						children = List(
							new Button("Undo") {
								maxWidth = Double.MaxValue
								onAction = e => controller.undo()
							},
							new Button("Redo") {
								maxWidth = Double.MaxValue
								onAction = e => controller.redo()
							},
							new Label("Rotate") {
								margin = Insets.apply(10, 0, 0, 0)
							},
							new Button("Left") {
								maxWidth = Double.MaxValue
								onAction = _ => controller.rotate(false)
							},
							new Button("Right") {
								maxWidth = Double.MaxValue
								onAction = _ => controller.rotate(true)
							},
							new Label("") {
								margin = Insets.apply(20, 0, 0, 0)
							},
							new Button("End turn") {
								maxWidth = Double.MaxValue
								onAction = _ => {
									controller.commit(placePeople.map(p => p.direction))
									placePeople = Option.empty
								}
							}
						)
					}
					right = new ScrollPane() {
						content = players
						fitToHeight = true
						fitToWidth = true
						hbarPolicy = ScrollBarPolicy.Never
						vbarPolicy = ScrollBarPolicy.AsNeeded
					}
				}
				private var anchor = Position2D(0d, 0d)
				onMousePressed = event => if (event.isSecondaryButtonDown) {
					anchor = Position2D(event.getSceneX, event.getSceneY)
				}
				onMouseDragged = event => if (event.isSecondaryButtonDown) {
					offset += Position2D((anchor.x - event.getSceneX) / size, (anchor.y - event.getSceneY) / size)
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
		stage.fullScreen = true

		controller.add(this)
		update()
	}

	def boardToLocal(pos: Position): Position2D =
		Position2D(pos.x * size + pane.width.value / 2d - size / 2d - offset.x * size, pos.y * size + pane.height.value / 2d - size / 2d - offset.y * size)

	def localToBoard(pos: Position2D): (Int, Int) =
		(((pos.x + offset.x * size + size / 2d - pane.width.value / 2d) / size).round.intValue, ((pos.y + offset.y * size + size / 2d - pane.height.value / 2d) / size).round.intValue)

	override def update(value: (Boolean, String) = (true, "")): Unit = {
		players.children = controller.board.players.map[Node](p => new VBox() {
			fillWidth = true
			margin = Insets.apply(0d, 0d, 30d, 0d)
			if (controller.board.getCurrentPlayer.get == p) {
				border = new Border(new BorderStroke(Color.Red, BorderStrokeStyle.Solid, CornerRadii.Empty, BorderWidths.Default))
			}
			children = List(
				new ColorPicker(p.color) {
					style = "-fx-color-label-visible: false ;"
					maxWidth = Double.MaxValue
					onAction = _ => {
						println(value.value)
						controller.setPlayerColor(p, new Color(value.value))
						update()
					}
				},
				new Label(p.name) {
					style = "-fx-font-size: 20pt;"
					if (controller.board.getCurrentPlayer.get == p) {
						style = style.value + "-fx-text-fill: red;"
					}
				},
				new Label(p.points + " Points"),
				new Label((controller.rules.maxPeople - p.people.size) + " People available")
			)
		}).toList.appended(
			new Button("Add Player") {
				maxWidth = Double.MaxValue
				onAction = _ => new TextInputDialog() {
					initOwner(stage)
					title = "Add player"
					contentText = "name:"
				}.showAndWait().foreach(name => {
					controller.addPlayer(name, randomColor())
					update()
				})
			}
		)
		if (controller.board.players.isEmpty) {
			info.text = "No players!"
			return
		}

		if (value._2.nonEmpty) {
			info.text = value._2
			infoTime = System.currentTimeMillis()
		} else if (System.currentTimeMillis() - infoTime > 5000) {
			info.text = "-"
		}
		Platform.runLater(() => {
			var moveable = Option.empty[Group]
			pane.children = controller.board.tiles.map(t => new Group {
				private val (pos, tile) = t
				private val screenPos = boardToLocal(pos)
				this.setUserData(t)
				children = generatePolygons(tile, screenPos, size)

				children.foreach(n => {
					n.setOnMouseEntered(e => {
						highlight = Option(SubPosition(pos, n.getUserData.asInstanceOf[Direction]))
						updateHighlight()
					})
					n.setOnMouseExited(e => {
						highlight = Option.empty
						updateHighlight()
					})
				})

				if (controller.board.currentPos.contains(pos)) {
					children.foreach(n => {
						n.setOnMousePressed(e => {
							if (e.isPrimaryButtonDown) {
								if (System.currentTimeMillis() - lastClick < 300) {
									lastClick = 0
									val dir = n.getUserData.asInstanceOf[Direction]
									if (placePeople.exists(sp => sp.position == pos && sp.direction == dir)) {
										placePeople = Option.empty
									} else {
										placePeople = Option(SubPosition(pos, dir))
									}
									update()
								} else {
									lastClick = System.currentTimeMillis()
								}
							}
						})
					})

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
					onMousePressed = e => if (e.isPrimaryButtonDown) {
						anchor = Position2D(e.getSceneX - translateX.value, e.getSceneY - translateY.value)
					} else if (e.isForwardButtonDown) {
						controller.rotate(false)
					} else if (e.isBackButtonDown) {
						controller.rotate(true)
					} else if (e.isMiddleButtonDown) {
						controller.commit(placePeople.map(p => p.direction))
						placePeople = Option.empty
					}
					onMouseDragged = e => if (e.isPrimaryButtonDown) {
						translateX.value = e.getSceneX - anchor.x
						translateY.value = e.getSceneY - anchor.y

						placePeopleNode.translateX = translateX.value
						placePeopleNode.translateY = translateY.value

						pane.children.remove(highlightGroup)
					}
					onMouseReleased = e => if (!e.isPrimaryButtonDown) {
						val board = controller.board
						board.currentPos.foreach(p => {
							controller.placeTile(p.x + (translateX.value / size).round.intValue, p.y + (translateY.value / size).round.intValue)

							if (controller.board.currentPos.isEmpty) {
								controller.placeTile(p.x, p.y)
							}
						})
						if (controller.board.currentPos.isDefined) {
							placePeople = placePeople.map(p => SubPosition(controller.board.currentPos.get, p.direction))
						}

						update()
					}
					moveable = Option(this)
				}
			})
			moveable.foreach(e => {
				pane.children.remove(e)
				pane.children.add(e)
			})

			controller.board.players.foreach(p =>
				p.people.foreach(sp => {
					pane.children.add(generateCircle(boardToLocal(sp._1), size, sp._2, p.color, ignoreMouse = true))
				})
			)

			placePeople.foreach(sp => {
				placePeopleNode = generateCircle(boardToLocal(sp.position), size, sp.direction, controller.board.getCurrentPlayer.get.color, ignoreMouse = true)
				pane.children.add(placePeopleNode)
			})

			pane.children.add(highlightGroup)
			updateHighlight()

			if (controller.board.currentTile.isEmpty) {
				pane.children.add(new Button {
					translateX.bind(pane.width - width)
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

	def updateHighlight(): Unit = {
		highlightGroup.children.clear()
		highlight.foreach(sp => {
			val island = controller.rules.findIsland(controller.board, sp)


			val shape = island.content.map(p => new Polygon {
				generatePoints(boardToLocal(p.position), size, p.direction).foreach(v => points.add(v))
			}).map(p => p.asInstanceOf[Shape]).reduce((p1, p2) => Shape.union(p1, p2))
			shape.fill = Color.Transparent
			shape.stroke = Color.Red
			shape.strokeWidth = Math.max(1, size / 50d)
			if (!island.complete) {
				shape.getStrokeDashArray.addAll(size / 25d, size / 25d)
			}
			highlightGroup.children.add(shape)
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
			generatePoints(pos, size, Direction.North).foreach(v => points.add(v))
			fill = colorOf(tile.north)
			userData = Direction.North
		},
		new Polygon {
			generatePoints(pos, size, Direction.East).foreach(v => points.add(v))
			fill = colorOf(tile.east)
			userData = Direction.East
		},
		new Polygon {
			generatePoints(pos, size, Direction.South).foreach(v => points.add(v))
			fill = colorOf(tile.south)
			userData = Direction.South
		},
		new Polygon {
			generatePoints(pos, size, Direction.West).foreach(v => points.add(v))
			fill = colorOf(tile.west)
			userData = Direction.West
		},
		new Polygon {
			generatePoints(pos, size, Direction.Center).foreach(v => points.add(v))
			fill = colorOf(tile.center)
			userData = Direction.Center
		}
	)

	def generatePoints(pos: Position2D, size: Double, direction: Direction): Vector[Double] =
		direction match {
			case Direction.North => Vector(
				pos.x,
				pos.y,
				pos.x + size * .2,
				pos.y + size * .2,
				pos.x + size * .8,
				pos.y + size * .2,
				pos.x + size,
				pos.y,
				pos.x,
				pos.y
			)
			case Direction.East => Vector(
				pos.x + size,
				pos.y,
				pos.x + size * .8,
				pos.y + size * .2,
				pos.x + size * .8,
				pos.y + size * .8,
				pos.x + size,
				pos.y + size,
				pos.x + size,
				pos.y
			)
			case Direction.South => Vector(
				pos.x,
				pos.y + size,
				pos.x + size * .2,
				pos.y + size * .8,
				pos.x + size * .8,
				pos.y + size * .8,
				pos.x + size,
				pos.y + size,
				pos.x,
				pos.y + size
			)
			case Direction.West => Vector(
				pos.x,
				pos.y,
				pos.x + size * .2,
				pos.y + size * .2,
				pos.x + size * .2,
				pos.y + size * .8,
				pos.x,
				pos.y + size,
				pos.x,
				pos.y
			)
			case Direction.Center => Vector(
				pos.x +.2 * size,
				pos.y +.2 * size,
				pos.x +.2 * size + size * .6,
				pos.y +.2 * size,
				pos.x +.2 * size + size * .6,
				pos.y +.2 * size + size * .6,
				pos.x +.2 * size,
				pos.y +.2 * size + size * .6,
				pos.x +.2 * size,
				pos.y +.2 * size
			)
		}

	def generateCircle(pos: Position2D, size: Double, direction: Direction, fil: Color, ignoreMouse: Boolean): Circle = direction match {
		case Direction.North => new Circle {
			radius =.09 * size
			centerX = pos.x +.5 * size
			centerY = pos.y +.1 * size
			fill = fil
			stroke = Color.Black
			mouseTransparent = ignoreMouse
		}
		case Direction.East => new Circle {
			radius =.09 * size
			centerX = pos.x +.9 * size
			centerY = pos.y +.5 * size
			fill = fil
			stroke = Color.Black
			mouseTransparent = ignoreMouse
		}
		case Direction.South => new Circle {
			radius =.09 * size
			centerX = pos.x +.5 * size
			centerY = pos.y +.9 * size
			fill = fil
			stroke = Color.Black
			mouseTransparent = ignoreMouse
		}
		case Direction.West => new Circle {
			radius =.09 * size
			centerX = pos.x +.1 * size
			centerY = pos.y +.5 * size
			fill = fil
			stroke = Color.Black
			mouseTransparent = ignoreMouse
		}
		case Direction.Center => new Circle {
			radius =.09 * size
			centerX = pos.x +.5 * size
			centerY = pos.y +.5 * size
			fill = fil
			stroke = Color.Black
			mouseTransparent = ignoreMouse
		}
	}

	def randomColor(): Color = Color.Red.deriveColor(Math.random() * 360, 1, 1, 1)
}
