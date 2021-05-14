package de.htwg.se.tiles.view.tui

abstract class CommandHandler(commands: String*) {
	private var nextHandler: Option[CommandHandler] = None

	final def handleCommand(command: String): Boolean = {
		if (commands.contains(command)) {
			handleSelf(command)
			return true
		}
		nextHandler.fold(false)(h => h.handleCommand(command))
	}

	def handleSelf(command: String): Unit

	final def appendHandler(handler: CommandHandler): CommandHandler = {
		nextHandler = Option(handler)
		handler
	}
}
