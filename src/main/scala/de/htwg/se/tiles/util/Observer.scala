package de.htwg.se.tiles.util

trait Observer[T] {
	def update(value: Event[T]): Unit
}

trait ObserverUnit {
	def update(): Unit
}
