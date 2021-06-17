package de.htwg.se.tiles

import com.google.inject.AbstractModule
import com.google.inject.assistedinject.FactoryModuleBuilder
import net.codingwell.scalaguice.{ScalaModule, typeLiteral}

import scala.reflect.runtime.universe.TypeTag

class ScalaFactoryModule() extends AbstractModule with ScalaModule {
	def bindModule[T: TypeTag, Timpl <: T : TypeTag, F: TypeTag] = install(new FactoryModuleBuilder().implement(typeLiteral[T], typeLiteral[Timpl]).build(typeLiteral[F]))
}
