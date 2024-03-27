package bstrees.implementations

import bstrees.templates.VertexTemplate

class SimpleVertex<K : Comparable<K>, V>(key: K, value: V) : VertexTemplate<K, V, SimpleVertex<K, V>>(key, value)

class AVLVertex<K : Comparable<K>, V>(key: K, value: V) : VertexTemplate<K, V, AVLVertex<K, V>>(key, value) {
    var diffHeight: Int = 0
}

class RBVertex<K : Comparable<K>, V>(key: K, value: V) : VertexTemplate<K, V, RBVertex<K, V>>(key, value) {
    var color: Color = Color.BLACK
    var nullType = true
    enum class Color {
        BLACK,
        RED,
    }
}
