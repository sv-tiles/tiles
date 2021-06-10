package de.htwg.se.tiles.util

trait Observable[T] {
	private var subscriber: Vector[Observer[T]] = Vector()

	def add(observer: Observer[T]): Unit = subscriber = subscriber :+ observer

	def remove(observer: Observer[T]): Unit = subscriber = subscriber.filter(e => e != observer)

	def notifyObservers(value: T): Unit = subscriber.foreach(e => e.update(value))
}
