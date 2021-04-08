case class Terrain(symbol: String) {
	override def toString: String = symbol
}

object Terrain extends Enumeration {
	val Plains = Terrain("_")
	val Hills = Terrain("^")
	val Forest = Terrain("T")
	val Mountains = Terrain("A")
	val Water = Terrain("~")
}

object Tile {
	val HEIGHT = 6
	val WIDTH_HALF = 6
	val BORDER = 2
	val MARGIN = 2
}

case class Tile(x: Int, y: Int, north: Terrain, east: Terrain, south: Terrain, west: Terrain, center: Terrain) {
	def printLine(line: Int): String = line match {
		case 0 => " " * Tile.MARGIN + north.symbol * Tile.WIDTH_HALF + " " * (Tile.MARGIN + Tile.BORDER)
		case 1 | 2 | 3 => west.symbol * 2 + center.symbol * Tile.WIDTH_HALF + east.symbol * Tile.BORDER + " " * Tile.MARGIN
		case 4 => " " * Tile.MARGIN + south.symbol * Tile.WIDTH_HALF + " " * (Tile.MARGIN + Tile.BORDER)
		case 5 => " " * 2 * Tile.WIDTH_HALF
	}

	override def toString: String = center.symbol
}

case class Map(tiles: Array[Tile], width: Int, height: Int) {
	require(width * height == tiles.length)

	override def toString: String =
		(for (y <- 0 until height; line <- 0 until Tile.HEIGHT) yield
			(for (x <- 0 until width) yield tiles(y * width + x).printLine(line)).mkString + "\n"
			).mkString
}

val tiles = Array(
	Tile(0, 0, Terrain.Water, Terrain.Plains, Terrain.Plains, Terrain.Water, Terrain.Water),
	Tile(1, 0, Terrain.Water, Terrain.Plains, Terrain.Forest, Terrain.Plains, Terrain.Plains),
	Tile(2, 0, Terrain.Plains, Terrain.Mountains, Terrain.Forest, Terrain.Hills, Terrain.Hills),

	Tile(0, 1, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains),
	Tile(1, 1, Terrain.Forest, Terrain.Forest, Terrain.Forest, Terrain.Plains, Terrain.Forest),
	Tile(2, 1, Terrain.Forest, Terrain.Mountains, Terrain.Mountains, Terrain.Forest, Terrain.Forest),

	Tile(0, 2, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Water),
	Tile(1, 2, Terrain.Forest, Terrain.Hills, Terrain.Mountains, Terrain.Plains, Terrain.Hills),
	Tile(2, 2, Terrain.Mountains, Terrain.Mountains, Terrain.Mountains, Terrain.Hills, Terrain.Mountains)
)

val map = Map(tiles, 3, 3)

