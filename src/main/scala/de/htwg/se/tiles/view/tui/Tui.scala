package de.htwg.se.tiles.view.tui

import de.htwg.se.tiles.control.controllerComponent.ControllerInterface
import de.htwg.se.tiles.util.Observer

import scala.util.Try

class Tui(controller: ControllerInterface, var width: Int, var height: Int, var scale: Int, var offset: (Int, Int)) extends Observer[(Boolean, String)] {
	require(scale >= 3)
	require(width >= 1)
	require(height >= 1)
	controller.add(this)


	var cursor: (Int, Int) = (0, 0)
	var result = Option.empty[String]
	var last: (Boolean, String) = (true, "")

	private val commandHandlers: CommandHandler = new CommandHandler("clear") {
		override def handleSelf(command: String): Unit = controller.clear()
	}

	commandHandlers.appendHandler(new CommandHandler("place", "u") {
		// TODO place people
		override def handleSelf(command: String): Unit = controller.commit(Option.empty)
	}).appendHandler(new CommandHandler("t") {
		override def handleSelf(command: String): Unit = {
			cursor = (cursor._1, cursor._2 - 1)
			controller.placeTile(cursor)
		}
	}).appendHandler(new CommandHandler("g") {
		override def handleSelf(command: String): Unit = {
			cursor = (cursor._1, cursor._2 + 1)
			controller.placeTile(cursor)
		}
	}).appendHandler(new CommandHandler("f") {
		override def handleSelf(command: String): Unit = {
			cursor = (cursor._1 - 1, cursor._2)
			controller.placeTile(cursor)
		}
	}).appendHandler(new CommandHandler("h") {
		override def handleSelf(command: String): Unit = {
			cursor = (cursor._1 + 1, cursor._2)
			controller.placeTile(cursor)
		}
	}).appendHandler(new CommandHandler("q", "r") {
		override def handleSelf(command: String): Unit = controller.rotate(false)
	}).appendHandler(new CommandHandler("e", "z") {
		override def handleSelf(command: String): Unit = controller.rotate(true)
	}).appendHandler(new CommandHandler("cursor reset") {
		override def handleSelf(command: String): Unit = {
			cursor = offset
			controller.placeTile(cursor)
		}
	}).appendHandler(new CommandHandler("undo") {
		override def handleSelf(command: String): Unit = {
			controller.undo()
			controller.board.currentPos.foreach(p => cursor = (p.x, p.y))
			update(last)
		}
	}).appendHandler(new CommandHandler("redo") {
		override def handleSelf(command: String): Unit = {
			controller.redo()
			controller.board.currentPos.foreach(p => cursor = (p.x, p.y))
			update(last)
		}
	})

	update((true, ""))

	def command(command: String): Option[String] = {
		result = Option.empty[String]
		if (command.matches("addPlayer .+")) {
			controller.addPlayer(command.split(" +", 2)(1))
			update((true, ""))
			return result
		}

		val cmd = command.replaceAll("\\W+", " ").trim()
		if (!commandHandlers.handleCommand(cmd)) {
			cmd match {
				case "w" => offset = offset.copy(_2 = offset._2 - 1)
				case "a" => offset = offset.copy(_1 = offset._1 - 1)
				case "s" => offset = offset.copy(_2 = offset._2 + 1)
				case "d" => offset = offset.copy(_1 = offset._1 + 1)
				case s"position $x $y" if Try(x.toInt).isSuccess && Try(y.toInt).isSuccess =>
					offset = (x.toInt, y.toInt)
				case "position" => result = Option("Position: " + offset._1 + " " + offset._2)
				case s"scale $f" if Try(f.toInt).isSuccess => scale = f.toInt
				case "scale" => result = Option("Scale: " + scale)
				case s"width $width" if Try(width.toInt).isSuccess => this.width = width.toInt
				case "width" => result = Option("Width: " + width)
				case s"height $height" if Try(height.toInt).isSuccess => this.height = height.toInt
				case "height" => result = Option("Height: " + height)
				case s"size $width $height" if Try(width.toInt).isSuccess && Try(height.toInt).isSuccess =>
					this.width = width.toInt
					this.height = height.toInt
				case "size" => result = Option("Size: " + width + " " + height)
				case "exit" => result = Option("stopping")
				case _ => result = Option("Unknown command: " + command)
			}
			update((true, ""))
		}


		result
	}

	override def update(value: (Boolean, String)): Unit = {
		last = value
		print("\u001b[2J")
		print(Console.BOLD)
		print(Console.GREEN)

		if (value._2 == "Already occupied") {
			println(getView)
		} else {
			println(getView.split("\n").map(l => """>.*<|^[^>]*<|>[^<]*$""".r.replaceAllIn(l, m => Console.RED + m.matched + Console.GREEN)).mkString("\n"))
		}
		print(Console.RESET)
		print(Console.UNDERLINED)
		if (result.isDefined) {
			println(result.get)
		}
		if (value._2.nonEmpty) {
			println(value._2)
		}
		print(Console.RESET)

		if (controller.board.players.isEmpty) {
			println(Console.RED + "No players!\n" + Console.RESET + "addPlayer <name>")
		} else {
			println("Player: " + Console.BOLD + controller.board.getCurrentPlayer.name + Console.RESET)
		}
	}


	def getView: String =
		controller.mapToString(offset, width, height, scale * 2, scale, Math.max(1, scale * .2).intValue, 2, frame = true, Option(cursor)) +
			"\n" + controller.currentTileToString(scale * 2, scale, Math.max(1, scale * .2).intValue, 2).fold[String](_ => "Error", s => s.trim()) + "\n\n"

}
