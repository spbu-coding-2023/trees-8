package bstrees.templates

abstract class VertexTemplate<K : Comparable<K>, V, Vertex_t : VertexTemplate<K, V, Vertex_t>>(val key: K, var value: V) {
    var parent: Vertex_t? = null
    var left: Vertex_t? = null
    var right: Vertex_t? = null

    operator fun compareTo(other: VertexTemplate<K, V, Vertex_t>): Int {
        return key.compareTo(other.key)
    }

    fun toPair(): Pair<K, V> {
        return Pair(key, value)
    }
}
