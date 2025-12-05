package com.github.novamage.typedmap

import scala.annotation.targetName

/** Represents a key within a [[TypedMap]].
  *
  * @tparam A The type of the values that can be associated with this key
  */
abstract class TypedKey[A] {

  private val fullyQualifiedName = getClass.getName

  /** Returns a [[TypedEntry]] by combining this key with the passed in value
    *
    * @param value The value to associate with this key
    */
  @targetName("arrow")
  def -> (value: A): TypedEntry[A] = new TypedEntry(this, value)

  override def hashCode(): Int = fullyQualifiedName.hashCode

  override def equals(obj: Any): Boolean = obj match {
    case that: TypedKey[_] => this.fullyQualifiedName == that.fullyQualifiedName
    case _                 => false
  }

  override def toString: String = getClass.getSimpleName.replace("$", "")
}
