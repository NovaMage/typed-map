package com.github.novamage.typedmap

import scala.reflect.ClassTag

/** Represents a key within a [[TypedMap]].
  *
  * @tparam A The type of the values that can be associated with this key
  */
abstract class TypedKey[A](implicit tag: ClassTag[A]) {

  /** Returns a [[TypedEntry]] by combining this key with the passed in value
    *
    * @param value The value to associate with this key
    */
  def -> (value: A): TypedEntry[A] = new TypedEntry(this, value)

  override def toString: String = s"TypedKey[${tag.runtimeClass.getSimpleName}]"
}
