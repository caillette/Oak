package io.github.caillette.oak

import io.github.caillette.oak.Kind.*
import io.github.caillette.oak.Axis.*
import java.util.ArrayList


class Tree(
    children : Sequence< Tree >,
    override val brand : Kind,
    public val payload : String
) : AbstractTree< Tree >( children ), BrandedTree< Tree, Kind > {
  override fun adopt( children : Sequence< Tree > ) : Tree {
    return Tree( children, brand, payload )
  }
}

enum class Kind : TreeBrand< Tree, Kind > {
  level
  title

  // TODO: Solve the typing problem preventing from moving all of that to the trait.

  fun plus() : LocationPath< Tree, Kind > {
    return LocationPath.of( this )
  }

  override fun mod( right : ( Treepath< Tree > ) -> Boolean ) : LocationPath< Tree, Kind > {
    return LocationPath.addPredicate( this, right )
  }

  override fun div( right : LocationPath< Tree, Kind > ) : LocationPath< Tree, Kind > {
    return LocationPath.addLocationPath( this, right )
  }

  override fun div( right : Axis ) : IncompleteLocationPath< Tree, Kind > {
    return LocationPath.addAxis( this, right )
  }

  override fun div( right : Kind ) : LocationPath< Tree, Kind > {
    return LocationPath.addNodetest( this, right )
  }
}


fun main( a : Array< String > ) {

  fun predicate( treepath : Treepath< Tree > ) : Boolean {
    return treepath is Treepath
  }

  println( +level )

  println( level / title )

  println( AncestorOrSelf % level )
  println( level % { it is Treepath } )  // Using ::predicate doesn't work.
  println( level % { it is Treepath } % { it is Treepath } )
  println( AncestorOrSelf % level % { it is Treepath } )
  println( AncestorOrSelf % level % { it is Treepath } % { it is Treepath } )

  println( AncestorOrSelf % level / title )
  println( level % { it is Treepath } / title )
  println( level % { it is Treepath } % { it is Treepath } / title )
  println( AncestorOrSelf % level % { it is Treepath } / title )
  println( AncestorOrSelf % level % { it is Treepath } % { it is Treepath } / title )

  println( AncestorOrSelf % level / AncestorOrSelf % title )
  println( level % { it is Treepath } / AncestorOrSelf % title )
  println( level % { it is Treepath } % { it is Treepath } / AncestorOrSelf % title )
  println( AncestorOrSelf % level % { it is Treepath } / AncestorOrSelf % title )
  println( AncestorOrSelf % level % { it is Treepath } % { it is Treepath } / AncestorOrSelf % title )

  println( level / AncestorOrSelf % level )
  println( level / ( AncestorOrSelf % level ) )
  println( level / level % { it is Treepath } )
  println( level / level % { it is Treepath } % { it is Treepath } )
  println( level / AncestorOrSelf % level % { it is Treepath } )
  println( level / AncestorOrSelf % level % { it is Treepath } % { it is Treepath } )
}