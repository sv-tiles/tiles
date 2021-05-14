package de.htwg.se.tiles.view.tui

trait CommandHandler {
	private var nextHandler: Option[CommandHandler] = None

	final def handleCommand(command: String): Boolean = {
		if (!handleSelf(command)) {
			return nextHandler.fold(false)(h => h.handleCommand(command))
		}
		true
	}

	def handleSelf(command: String): Boolean

	final def appendHandler(handler: CommandHandler): CommandHandler = {
		nextHandler = Option(handler)
		handler
	}
}
