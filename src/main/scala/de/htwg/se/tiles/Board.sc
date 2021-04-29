import de.htwg.se.tiles.model.{Map, Terrain, Tile}

val tiles = Array(
	Tile(Terrain.Water, Terrain.Plains, Terrain.Plains, Terrain.Water, Terrain.Water),
	Tile(Terrain.Water, Terrain.Plains, Terrain.Forest, Terrain.Plains, Terrain.Plains),
	Tile(Terrain.Plains, Terrain.Mountains, Terrain.Forest, Terrain.Hills, Terrain.Hills),

	Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains),
	Tile(Terrain.Forest, Terrain.Forest, Terrain.Forest, Terrain.Plains, Terrain.Forest),
	Tile(Terrain.Forest, Terrain.Mountains, Terrain.Mountains, Terrain.Forest, Terrain.Forest),

	Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Water),
	Tile(Terrain.Forest, Terrain.Hills, Terrain.Mountains, Terrain.Plains, Terrain.Hills),
	Tile(Terrain.Mountains, Terrain.Mountains, Terrain.Mountains, Terrain.Hills, Terrain.Mountains)
)

val tile = Tile(Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains, Terrain.Plains)
val map = Map().add((0, 0), tile).add((1, 0), tile)
print(map.toString)
/*
	var map = Map()
	map = map.add((0, 0), tiles(0))
	print(map)
	map = map.add((1, 0), tiles(1))
	map = map.add((2, 0), tiles(2))
	map = map.add((0, 1), tiles(3))
	map = map.add((0, 2), tiles(6))
	print(map)
*/
// map.toString(8, 3, 2, 2)
