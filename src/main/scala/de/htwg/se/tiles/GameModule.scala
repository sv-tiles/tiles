package de.htwg.se.tiles

import de.htwg.se.tiles.control.controllerComponent.ControllerInterface
import de.htwg.se.tiles.control.controllerComponent.controllerBaseImpl.Controller
import de.htwg.se.tiles.model.boardComponent.BoardInterface
import de.htwg.se.tiles.model.boardComponent.boardBaseImpl.Board
import de.htwg.se.tiles.model.playerComponent.playerBaseImpl.PlayerBase
import de.htwg.se.tiles.model.playerComponent.{PlayerFactory, PlayerInterface}
import de.htwg.se.tiles.model.rulesComponent.RulesInterface
import de.htwg.se.tiles.model.rulesComponent.rulesBaseImpl.RulesBase

case class GameModule() extends ScalaFactoryModule {
	override def configure(): Unit = {
		bind[RulesInterface].to[RulesBase]
		bindFactory[PlayerInterface, PlayerBase, PlayerFactory]
		bind[BoardInterface].to[Board]
		bind[ControllerInterface].to[Controller]
	}
}
