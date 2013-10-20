package io.github.caillette.oak


/**
 * Minimal behavior of an immutable, strong-typed tree structure.
 *
 * This compiles:
 * <pre>
  for( i in tree.indices ) { tree[ i ] }
 * </pre>
 */
trait AnyTree< TREE : AnyTree< TREE > > {
  fun get( i : Int ) : TREE
  val indices : IntRange
  fun adopt( children : Array< TREE > ) : TREE
}

abstract class AbstractTree< TREE : AnyTree< TREE > >(
    children : Array< TREE >
) : AnyTree< TREE > {

  private val children : Array< TREE > = children.copyOf()

  override fun get( i : Int ) : TREE { return children[ i ] }

  override val indices : IntRange
    get() = children.indices
}


class Treepath< TREE : AnyTree< TREE > >(
    val previous : Treepath< TREE >?,
    val indexInPrevious : Int?,
    val treeAtEnd : TREE
) {
  fun Treepath( tree : TREE ) {
    Treepath( null, null, tree )
  }

  fun Treepath( previous : Treepath< TREE >, indexInPrevious : Int ) {
    Treepath( previous, indexInPrevious, previous.treeAtEnd[ indexInPrevious ] )
  }
}


