package de.htwg.se.tiles.model.boardComponent

import scala.reflect.runtime.universe.typeOf

sealed trait Terrain {
	def symbol: String

	override def toString: String = symbol
}

object Terrain {
	case object Plains extends Terrain {
		override val symbol: String = "_"
	}

	case object Hills extends Terrain {
		override val symbol: String = "^"
	}

	case object Forest extends Terrain {
		override val symbol: String = "T"
	}

	case object Mountains extends Terrain {
		override val symbol: String = "A"
	}

	case object Water extends Terrain {
		override val symbol: String = "~"
	}

	lazy val defaults: Iterable[Terrain] = typeOf[Terrain].companion.members.filter(s => s.isModule).map(s => reflect.runtime.currentMirror.reflectModule(s.asModule).instance.asInstanceOf[Terrain])
}
