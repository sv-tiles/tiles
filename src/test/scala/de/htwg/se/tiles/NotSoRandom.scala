package de.htwg.se.tiles

class NotSoRandom(private var i: Int = 0) {
	def next(max: Int): Int = {
		i = i * 2 + 3
		((i % max) + max) % max
	}
}
