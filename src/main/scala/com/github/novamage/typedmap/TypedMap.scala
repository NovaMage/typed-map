package com.github.novamage.typedmap

import scala.annotation.targetName

/** Utility class that allows storing and retrieving values in a map-like interface with the
  * added safety that values must match the type of the [[com.github.novamage.typedmap.TypedKey]] they are associated with,
  * thus avoiding casting errors.
  */
trait TypedMap {

  /** Returns a new [[com.github.novamage.typedmap.TypedMap]] with the specified entry added
    *
    * @param kv The entry to add
    * @tparam A The type of the value
    */
  @targetName("addEntry")
  def + [A](kv: TypedEntry[A]): TypedMap

  /** Returns a new [[com.github.novamage.typedmap.TypedMap]] with the specified key and its value removed
    *
    * @param key The key of the entry to remove
    * @tparam A The type of the value
    */
  @targetName("removeEntry")
  def - [A](key: TypedKey[A]): TypedMap

  /** Returns the value associated with the specified key
    *
    * @param key The [[com.github.novamage.typedmap.TypedKey]] of the entry to retrieve
    * @tparam A The type of the value retrieved
    */
  def apply[A](key: TypedKey[A]): A

  /** Returns <code>Some(value)</code> if one is associated with the key, <code>None</code> otherwise
    *
    * @param key The [[com.github.novamage.typedmap.TypedKey]] of the entry to retrieve
    * @tparam A The type of the value retrieved
    */
  def get[A](key: TypedKey[A]): Option[A]

  /** Returns an iterable of all the typed keys in this map */
  def keys: Iterable[TypedKey[?]]

  /** Returns a new [[com.github.novamage.typedmap.TypedMap]] with the entries of this map and the entries of the passed in map
    *
    * Any entries existing in both maps will be overwritten by the entries in the argument map
    *
    * @param another The map to merge with
    */
  def merge(another: TypedMap): TypedMap

}

private class DefaultTypedMap(wrapped: Map[Any, Any]) extends TypedMap {

  @targetName("addEntry")
  def + [A](entry: TypedEntry[A]): DefaultTypedMap = new DefaultTypedMap(wrapped.+((entry.key, entry.value)))

  @targetName("removeEntry")
  def - [A](key: TypedKey[A]): DefaultTypedMap = new DefaultTypedMap(wrapped.-(key))

  def apply[A](key: TypedKey[A]): A = wrapped(key).asInstanceOf[A]

  def get[A](key: TypedKey[A]): Option[A] = wrapped.get(key).map(_.asInstanceOf[A])

  def keys: Iterable[TypedKey[?]] = wrapped.keys.map(_.asInstanceOf[TypedKey[?]])

  def merge(another: TypedMap): TypedMap = {
    another.keys.foldLeft(this) { (acc, key) =>
      acc.+(key.asInstanceOf[TypedKey[Any]] -> another(key))
    }
  }

  override def toString: String = s"TypedMap(${wrapped.map(_.toString()).mkString(", ")})"

}

/** Factory for [[TypedMap]] instances */
//noinspection ScalaWeakerAccess
object TypedMap {

  /** Returns an empty [[com.github.novamage.typedmap.TypedMap]] */
  val empty: TypedMap = new DefaultTypedMap(Map.empty)

  /** Returns a [[com.github.novamage.typedmap.TypedMap]] with the specified entries
    *
    * @param entries The entries to add to the map
    */
  def apply(entries: TypedEntry[?]*): TypedMap = {
    new DefaultTypedMap(entries.map(x => (x.key, x.value)).toMap)
  }

}
