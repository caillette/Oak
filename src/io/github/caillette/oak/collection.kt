package io.github.caillette.oak

/**
 * Kind of immutable array.
 */
class Sequence< ELEMENT >( elements : Array< ELEMENT > ) {

  private val elements = elements.copyOf()

  fun Sequence( element : ELEMENT ) {
    Sequence( array( element ) )
  }

  val indices : IntRange = elements.indices

  fun get( i : Int ) = elements[ i ]

  fun plus( element : ELEMENT ) : Sequence< ELEMENT > {
    return Sequence( Array(
        elements.size + 1,
        { if( it < elements.size ) elements[ it ] else element }
    ) )
  }

  class object {
    fun< ELEMENT > empty() = Sequence< ELEMENT >( array() )
  }

}