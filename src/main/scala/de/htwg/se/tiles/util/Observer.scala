package de.htwg.se.tiles.util

trait Observer[T] {
	def update(value: T): Unit
}
