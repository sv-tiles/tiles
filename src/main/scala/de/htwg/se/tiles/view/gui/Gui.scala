package de.htwg.se.tiles.view.gui

import de.htwg.se.tiles.control.Controller
import de.htwg.se.tiles.util.Observer
import scalafx.Includes.when
import scalafx.application.{JFXApp3, Platform}
import scalafx.scene.Scene
import scalafx.scene.control.{Menu, MenuBar}
import scalafx.scene.layout.{BorderPane, Pane}
import scalafx.scene.paint.Color.{Green, Red}
import scalafx.scene.shape.Rectangle


class Gui(val controller: Controller) extends JFXApp3 with Observer[(Boolean, String)] {
	val pane: Pane = new Pane {
		style = "-fx-background-color: rgb(50,50,50)"
	}

	override def start(): Unit = {
		stage = new JFXApp3.PrimaryStage {
			title.value = "Tiles"
			width = 600
			height = 450
			scene = new Scene {
				root = new BorderPane {
					top = new MenuBar {
						menus = List(new Menu("Test"))
					}
					center = pane
				}
			}
		}


		controller.add(this)
	}

	override def update(value: (Boolean, String)): Unit = {
		Platform.runLater(() => {
			pane.children = new Rectangle {
				x = 5
				y = 5
				width = 100
				height = 100
				fill <== when(hover) choose Green otherwise Red
			}
		})
	}
}
