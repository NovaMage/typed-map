package com.github.novamage.typedmap

/** Represents an entry within a [[TypedMap]].  You may conveniently create instances of this class using the
  * [[TypedKey.-> ->]] method on [[TypedKey]]
  *
  * @param key The key of the entry
  * @param value The value of the entry, which must be of the same type as the key
  * @tparam A The type of the value
  */
final class TypedEntry[A](val key: TypedKey[A], val value: A) {

  override def toString: String = s"$key -> $value"

}
