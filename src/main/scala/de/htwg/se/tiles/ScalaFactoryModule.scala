package de.htwg.se.tiles

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import net.codingwell.scalaguice.{ScalaModule, typeLiteral}

import scala.reflect.runtime.universe.TypeTag

abstract class ScalaFactoryModule extends AbstractModule with ScalaModule {
	def bindFactory[T: TypeTag, Timpl <: T : TypeTag, F: TypeTag]: Unit =
		install(new FactoryModuleBuilder().implement(typeLiteral[T], typeLiteral[Timpl]).build(typeLiteral[F]))
}
