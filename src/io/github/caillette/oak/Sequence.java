package io.github.caillette.oak;

import jet.IntRange;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * Kotlin-aware immutable indexed collection.
 *
 * @author Laurent Caillette
 */
public final class Sequence< ELEMENT > {

  private final ELEMENT[] elements ;

  @SafeVarargs
  public Sequence( ELEMENT... elements ) {
    this.elements = elements.length == 0 ? elements : Arrays.copyOf( elements, elements.length ) ;
    for ( int i = 0 ; i < elements.length ; i++ ) {
      if ( elements[ i ] == null ) {
        throw new IllegalArgumentException( "Null element at [ " + i  + "]" ) ;
      }
    }
  }

  @NotNull
  public ELEMENT get( final int index ) {
    return elements[ index ] ;
  }

  /**
   * Returns a range suitable for index-based iteration.
   * Implementing {@link jet.Progression} and {@link jet.Range} doesn't allow an index-based
   * for loop with something like {@code for( i in mySequence ) ...}.
   * This construct may seem inelegant in Kotlin.
   * Performance concern: we try to save some memory by rebuilding the result at each call.
   */
  @NotNull
  public IntRange indices() {
    return elements.length == 0 ? IntRange.EMPTY : new IntRange( 0, elements.length - 1 ) ;
  }

  @NotNull
  public Sequence< ELEMENT > plus( @NotNull final ELEMENT appended ) {
    final ELEMENT[] newElements =  Arrays.copyOf( elements, elements.length + 1 ) ;
    newElements[ elements.length ] = appended ;
    return new Sequence<>( newElements ) ;
  }

  public int size() {
    return elements.length ;
  }

  @NotNull
  public Sequence< ELEMENT > plus( @NotNull final Sequence< ELEMENT > appended ) {
    final int newSize = this.size() + appended.size() ;
    if( newSize == 0 ) {
      return new Sequence<>( Arrays.copyOf( elements, 0 ) ) ;
    } else {
      final ELEMENT[] newElements =  Arrays.copyOf( elements, newSize ) ;
      System.arraycopy( appended.elements, 0, newElements, this.size(), appended.size() ) ;
      return new Sequence<>( newElements ) ;
    }
  }

}
