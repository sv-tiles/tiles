package de.htwg.se.tiles.util

;

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec;

class ObserverSpec extends AnyWordSpec with Matchers {
	"A observer-pattern" when {
		"generics in use" should {
			var result = 0
			class TestObserver extends Observer[Int] {
				override def update(value: Event[Int]): Unit = result = value.data
			}
			class TestObservable extends Observable[Int] {

			}
			"work" in {
				val observer = new TestObserver()
				val observable = new TestObservable()

				observable.add(observer)

				val testNumber = 123
				observable.notifyObservers(Event(testNumber))

				result shouldBe testNumber

				result = 0

				observable.remove(observer)
				observable.notifyObservers(Event(testNumber))

				result shouldBe 0
			}
		}
		"no generics in use" should {
			val testNumber = 123
			var result = 0
			class TestObserver extends ObserverUnit {
				override def update(): Unit = result = testNumber
			}
			class TestObservable extends ObservableUnit {

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
	"A event" should {
		"work" in {
			val ev1 = Event((1, 2, 3))
			Event.unapply(ev1).get shouldBe(1, 2, 3)

			val ev2 = Event(1, 2, 3)
			Event.unapply(ev2).get shouldBe(1, 2, 3)

			val ev3 = Event("test")
			Event.unapply(ev3).get shouldBe "test"
		}
	}
}
