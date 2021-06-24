package de.htwg.se.tiles.util

class RecordingObserver extends Observer[(Boolean, String)] {
	var list: Vector[(Boolean, String)] = Vector[(Boolean, String)]()

	override def update(value: (Boolean, String)): Unit = list = list.appended(value)
}
