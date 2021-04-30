package de.htwg.se.tiles.util

class Observable[T] {
	private var subscriber: Vector[Observer[T]] = Vector()

	def add(observer: Observer[T]): Unit = subscriber = subscriber :+ observer

	def remove(observer: Observer[T]): Unit = subscriber = subscriber.filter(e => e != observer)

	def notifyObservers(value: Event[T]): Unit = subscriber.foreach(e => e.update(value))
}

class ObservableUnit {
	private var subscriber: Vector[ObserverUnit] = Vector()

	def add(observer: ObserverUnit): Unit = subscriber = subscriber :+ observer

	def remove(observer: ObserverUnit): Unit = subscriber = subscriber.filter(e => e != observer)

	def notifyObservers(): Unit = subscriber.foreach(e => e.update())
}
