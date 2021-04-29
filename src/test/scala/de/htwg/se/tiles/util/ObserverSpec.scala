package de.htwg.se.tiles.util

;

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec;

class ObserverSpec extends AnyWordSpec with Matchers {
	"A observer" when {
		"generics in use" should {
			var result = 0
			class TestObserver extends Observer[Int] {
				override def update(value: Int): Unit = result = value
			}
			class TestObservable extends Observable[Int] {

			}
			"work" in {
				val observer = new TestObserver()
				val observable = new TestObservable()

				observable.add(observer)

				val testNumber = 123
				observable.notifyObservers(testNumber)

				result shouldBe testNumber

				result = 0

				observable.remove(observer)
				observable.notifyObservers(testNumber)

				result shouldBe 0
			}
		}
		"no generics in use" should {
			val testNumber = 123
			var result = 0
			class TestObserver extends Observer[Unit] {
				override def update(value: Unit): Unit = result = testNumber
			}
			class TestObservable extends Observable[Unit] {

			}
			"work" in {
				val observer = new TestObserver()
				val observable = new TestObservable()

				observable.add(observer)
				observable.notifyObservers()

				result shouldBe testNumber

				result = 0

				observable.remove(observer)
				observable.notifyObservers()

				result shouldBe 0
			}
		}
	}

}
