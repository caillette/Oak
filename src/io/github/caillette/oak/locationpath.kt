package io.github.caillette.oak

/**
 * A {@link AnyTree tree} with each node labelled with an enum element.
 */
trait BrandedTree<
    TREE : BrandedTree< TREE, BRAND >,
    BRAND : TreeBrand< TREE, BRAND >
> : AnyTree< TREE > {
  val brand : BRAND
}

/**
 * A well-known kind of node that corresponds to an XML element name.
 * In XPath specification, a Node Test is an attribute, a namespace or an element.
 * We simplify here to support only the element name.
 */
trait TreeBrand<
    TREE : BrandedTree< TREE, BRAND >,
    BRAND : TreeBrand< TREE, BRAND >
> : Enum< BRAND > {

  fun mod( right : ( Treepath< TREE > ) -> Boolean ) : LocationPath< TREE, BRAND > /*{
    return LocationPath.addPredicate( this, right )
  }*/
  fun div( right : LocationPath< TREE, BRAND > ) : LocationPath< TREE, BRAND >
  fun div( right : BRAND ) : LocationPath< TREE, BRAND >
  fun div( right : Axis ) : IncompleteLocationPath< TREE, BRAND >

}

/**
 * A complex predicate on a {@link BrandedTree tree}.
 * The term of "Location Path" has the same meaning as in XPath specification.
 */
class LocationPath<
    NODE : BrandedTree< NODE, NODETEST >,
    NODETEST : TreeBrand< NODE, NODETEST >
>(
    val left : LocationPath< NODE, NODETEST >? = null,
    val step : LocationStep< NODE, NODETEST >
) {

  fun leftmost() : LocationPath< NODE, NODETEST > {
    var previous = this ;
    while( true ) {
      if( previous.left != null ) {
        previous = previous.left!!
      } else {
        break
      }
    }
    return previous
  }

  /**
   * Appends given {@link LocationPath} on the left, which means replacing the null reference.
   * This function is recursive because we don't expect more than 100 levels.
   * According to the Internet wisdom, with default stack size most of JVM support 1000 levels.
   */
  private fun chainLeft(
      newLeft : LocationPath< NODE, NODETEST >
  ) : LocationPath< NODE, NODETEST > {
    if( left == null ) {
      return LocationPath< NODE, NODETEST >( newLeft, step )
    } else {
      return chainLeft( newLeft )
    }
  }

  fun div( right : LocationPath< NODE, NODETEST > ) : LocationPath< NODE, NODETEST > {
    return right.chainLeft( this )
  }

  fun div( right : NODETEST ) : LocationPath< NODE, NODETEST > {
    return LocationPath( this, LocationStep( nodetest = right ) )
  }

  fun div( right : Axis ) : IncompleteLocationPath< NODE, NODETEST > {
    return IncompleteLocationPath( this, right )
  }

  fun mod( predicate : ( Treepath< NODE > ) -> Boolean ) : LocationPath< NODE, NODETEST > {
    return LocationPath(
        this.left,
        LocationStep( step.axis, step.nodetest, step.predicates + predicate )
    )
  }

  fun toString() : String {
    var result = ""
    var current : LocationPath< NODE, NODETEST >? = this
    do {
      result = current?.step?.toString() +
      ( if( result.isEmpty() ) "" else " <- " ) +
      result
      current = current?.left
    } while( current != null )
    return javaClass.getSimpleName() + "{ " + result + " }"

  }


  class object {

    fun <
        NODE : BrandedTree< NODE, NODETEST >,
        NODETEST : TreeBrand< NODE, NODETEST >
    > of( nodetest: NODETEST ) : LocationPath< NODE, NODETEST >
    {
      return LocationPath( null, LocationStep( nodetest = nodetest) )
    }

    fun <
        NODE : BrandedTree< NODE, NODETEST >,
        NODETEST : TreeBrand< NODE, NODETEST >
    > addPredicate(
        nodetest : NODETEST,
        predicate : ( Treepath< NODE > ) -> Boolean
    ) : LocationPath< NODE, NODETEST >
    {
      return LocationPath(
          null,
          LocationStep(
              nodetest = nodetest,
              predicates = Sequence( predicate )
          )
      )
    }

    fun <
        NODE : BrandedTree< NODE, NODETEST >,
        NODETEST : TreeBrand< NODE, NODETEST >
    > addLocationPath(
        nodetest : NODETEST,
        locationPath : LocationPath< NODE, NODETEST >
    ) : LocationPath< NODE, NODETEST >
    {
      return locationPath.chainLeft( of( nodetest ) )
    }

    fun <
        NODE : BrandedTree< NODE, NODETEST >,
        NODETEST : TreeBrand< NODE, NODETEST >
    > addAxis(
        nodetest : NODETEST,
        axis : Axis
    ) : IncompleteLocationPath< NODE, NODETEST >
    {
      return IncompleteLocationPath( of( nodetest ), axis )
    }

    fun <
        NODE : BrandedTree< NODE, NODETEST >,
        NODETEST : TreeBrand< NODE, NODETEST >
    > addNodetest(
        nodetest : NODETEST,
        right : NODETEST
    ) : LocationPath< NODE, NODETEST >
    {
      return LocationPath(
          LocationPath( null, LocationStep( nodetest = nodetest ) ),
          LocationStep( nodetest = right )
      )
    }


  }

}


/**
 * Describes one step in a {@link LocationPath}.
 * The term of "Location Step" has the same meaning as in XPath specification.
 */
class LocationStep<
    NODE : BrandedTree< NODE, NODETEST >,
    NODETEST : TreeBrand< NODE, NODETEST >
>(
    val axis : Axis = Axis.Child,
    val nodetest : NODETEST,
    val predicates : Sequence< ( ( Treepath< NODE > ) -> Boolean ) > = Sequence()
) {

  fun toString() : String {
    var result = axis.name() + "::" + nodetest.name() + "["
    for( predicate in predicates.indices ) {
      if( predicate > 0 ) result += ","
      result += "?"
    }
    result += "]"

    return javaClass.getSimpleName() + "{ " + result + " }"
  }

}

/**
 * Because operator evalutes from left to right, we need to capture the result of an expression
 * which ends by an {@link Axis} before going any further.
 */
class IncompleteLocationPath<
    NODE : BrandedTree< NODE, NODETEST >,
    NODETEST : TreeBrand< NODE, NODETEST >
>(
    val left : LocationPath< NODE, NODETEST >,
    val axis : Axis
) {
  fun mod( nodetest : NODETEST ) : LocationPath< NODE, NODETEST > {
    return LocationPath( left, LocationStep( axis, nodetest ) )
  }
}
