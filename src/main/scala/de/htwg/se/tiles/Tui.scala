package de.htwg.se.tiles

case class Tui(width: Int, height: Int, scale: Int, offset: (Int, Int), map: Map) {
	require(scale >= 3)
	require(width >= 1)
	require(height >= 1)

	def command(command: String): Tui = command match {
		case "w" => this.copy(offset = offset.copy(_2 = offset._2 - 1))
		case "a" => this.copy(offset = offset.copy(_1 = offset._1 - 1))
		case "s" => this.copy(offset = offset.copy(_2 = offset._2 + 1))
		case "d" => this.copy(offset = offset.copy(_1 = offset._1 + 1))
		case s"rand $x $y" if x.forall(_.isDigit) && y.forall(_.isDigit) => copy(map = map.add((x.toInt, y.toInt), Tile.random()))
		case "exit" =>
			println("stopping")
			this
		case _ =>
			println("Unknown command: " + command)
			this
	}


}
