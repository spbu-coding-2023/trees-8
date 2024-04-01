package trees.implementations

import trees.templates.VertexTemplate

class SimpleVertex<K : Comparable<K>, V>(key: K, value: V) : VertexTemplate<K, V, SimpleVertex<K, V>>(key, value)

class AVLVertex<K : Comparable<K>, V>(key: K, value: V) : VertexTemplate<K, V, AVLVertex<K, V>>(key, value) {
    var diffHeight: Int = 0
        internal set(value) {
            if (value !in -2..2) {
                throw IllegalArgumentException("difference of heights can't be out of [-2,2]")
            }
            field = value
        }
}

class RBVertex<K : Comparable<K>, V>(key: K, value: V) : VertexTemplate<K, V, RBVertex<K, V>>(key, value) {
    var color: Color = Color.BLACK
    var nullType = false

    enum class Color {
        BLACK,
        RED,
    }
}
