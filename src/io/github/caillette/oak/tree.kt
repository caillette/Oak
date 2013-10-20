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
  val children : Sequence< TREE >
  fun adopt( children : Sequence< TREE > ) : TREE
}

abstract class AbstractTree< TREE : AnyTree< TREE > >(
    override val children : Sequence< TREE >
) : AnyTree< TREE > {

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
    Treepath( previous, indexInPrevious, previous.treeAtEnd.children[ indexInPrevious ] )
  }
}


