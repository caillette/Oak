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

  @NotNull
  public final IntRange indices ;

  private Sequence(
      final @NotNull ELEMENT[] elements,
      final @NotNull IntRange indices
  ) {
    this.elements = elements ;
    this.indices = indices ;
  }

  @SafeVarargs
  public Sequence( ELEMENT... elements ) {
    this(
        elements.length == 0 ? elements : Arrays.copyOf( elements, elements.length ),
        elements.length == 0 ? IntRange.EMPTY : new IntRange( 0, elements.length - 1 )
    ) ;
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

  @NotNull
  public Sequence< ELEMENT > plus( @NotNull final ELEMENT appended ) {
    final ELEMENT[] newElements =  Arrays.copyOf( elements, elements.length + 1 ) ;
    newElements[ elements.length ] = appended ;
    final IntRange newRange = new IntRange( 0, elements.length ) ;
    return new Sequence<>( newElements, newRange ) ;
  }

  static boolean isEmpty( @NotNull final IntRange intRange ) {
    return intRange.getEnd() > intRange.getStart() ;
  }

  static int size( @NotNull final IntRange intRange ) {
    return isEmpty( intRange ) ? 0 : intRange.getEnd() - intRange.getStart() + 1 ;
  }

  @NotNull
  public Sequence< ELEMENT > plus( @NotNull final Sequence< ELEMENT > appended ) {
    final int thisSize = size( indices ) ;
    final int appendedSize = size( appended.indices ) ;
    final int newSize = thisSize + appendedSize ;
    if( newSize == 0 ) {
      return new Sequence<>( Arrays.copyOf( elements, 0 ), IntRange.EMPTY ) ;
    } else {
      final ELEMENT[] newElements =  Arrays.copyOf( elements, newSize ) ;
      System.arraycopy( appended.elements, 0, newElements, thisSize, appendedSize ) ;
      final IntRange newRange = new IntRange( 0, newSize - 1 ) ;
      return new Sequence<>( newElements, newRange ) ;
    }
  }

}
