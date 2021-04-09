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
}

case class Tile(x: Int, y: Int, north: Terrain, east: Terrain, south: Terrain, west: Terrain, center: Terrain) {
	def printLine(line: Int, width: Int, height: Int, border: Int, margin: Int): String = {
		if (line < border / 2) {
			" " * border + north.symbol * (width - (2 * border)) + " " * (border + margin)
		} else if (line >= height) {
			" " * (width + margin)
		} else if (line >= height - border / 2) {
			" " * border + south.symbol * (width - (2 * border)) + " " * (border + margin)
		} else {
			west.symbol * border + center.symbol * (width - (2 * border)) + east.symbol * border + " " * margin
		}
	}

	override def toString: String = center.symbol
}

case class Map(tiles: Array[Tile], width: Int, height: Int) {
	require(width * height == tiles.length)

	override def toString: String = {
		val tileHeight = 12
		val tileWidth = 5
		val border = 2
		val margin = 2
		toString(tileWidth, tileHeight, border, margin)
	}

	def toString(tileWidth: Int, tileHeight: Int, border: Int, margin: Int): String = {
		require(border >= 2)
		require(margin >= 0)
		require(tileWidth - border >= 1)
		require(tileHeight - border / 2 >= 1)

		(for (y <- 0 until height; line <- 0 until tileHeight + margin / 2) yield
			(for (x <- 0 until width) yield this.tiles(y * width + x).printLine(line, tileWidth, tileHeight, border, margin)).mkString + "\n"
			).mkString
	}
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

map.toString(8, 3, 2, 2)
