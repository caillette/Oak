package io.github.caillette.oak


/**
 * Describes <a href="http://www.w3.org/TR/xpath/#axes">XPath Axes</a> with some simplifications.
 * <p>
 * Because we don't deal with other Node Types than {@code node()} the following simplifications
 * apply.
 * <ul>
 *   <li>
 *     {@code //}, the shorthand for {@code /descendant-or-self::node()/} becomes
 *     {@link DescendantOfSelf}.
 *   </li>
 *   <li>
 *     {@code .}, the shorthand for {@code self::node()} becomes {@link Self}.
 *   </li>
 * </ul>
 * <p>
 * The {@link Child} Axis remains optional. Documentation says:
 * <blockquote>
 *   The most important abbreviation is that {@code child::} can be omitted from a location step.
 *   In effect, child is the default axis.
 *   For example, a location path {@code div/para} is short for {@code child::div/child::para}.
 * </blockquote>
 *
 * @author Laurent Caillette
 */
enum class Axis {

  /**
   * The <b>child</b> axis contains the children of the context node.
   */
  Child

  /**
   * The <b>descendant</b> axis contains the descendants of the context node; a descendant is
   * a child or a child of a child and so on; thus the descendant axis never contains attribute
   * or namespace nodes.
   */
  Descendant

  /**
   * The parent axis contains the parent of the context node, if there is one.
   */
  Parent

  /**
   * The ancestor axis contains the ancestors of the context node; the ancestors of the context
   * node consist of the parent of context node and the parent's parent and so on;
   * thus, the ancestor axis will always include the root node, unless the context node is the
   * root node.
   */
  Ancestor

  /**
   * The following-sibling axis contains all the following siblings of the context node;
   * if the context node is an attribute node or namespace node, the following-sibling axis is
   * empty.
   */
  FollowingSibling

  /**
   * The preceding-sibling axis contains all the preceding siblings of the context node;
   * if the context node is an attribute node or namespace node, the preceding-sibling axis is
   * empty.
   */
  PrecedingSibling

  /**
   * The following axis contains all nodes in the same document as the context node that are
   * after the context node in document order, excluding any descendants and excluding attribute
   * nodes and namespace nodes.
   */
  Following

  /**
   * The preceding axis contains all nodes in the same document as the context node that are
   * before the context node in document order, excluding any ancestors and excluding attribute
   * nodes and namespace nodes.
   */
  Preceding

  // Attribute (skipped)

  // Namespace (skipped)

  /**
   * The self axis contains just the context node itself.
   */
  Self

  /**
   * The descendant-or-self axis contains the context node and the descendants
   * of the context node.
   */
  DescendantOrSelf

  /**
   * The ancestor-or-self axis contains the context node and the ancestors of the context node;
   * thus, the ancestor axis will always include the root node.
   */
  AncestorOrSelf


  fun<
      NODE : BrandedTree< NODE, NODETEST >,
      NODETEST : TreeBrand< NODE, NODETEST >
  > mod(
      nodetest : NODETEST
  ) : LocationPath< NODE, NODETEST > {
    return LocationPath( null, LocationStep( this, nodetest ) )
  }


}
