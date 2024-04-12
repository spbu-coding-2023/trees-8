package trees.templates

abstract class Vertex<K : Comparable<K>, V, Vertex_t : Vertex<K, V, Vertex_t>>(key: K, value: V) {
    var key = key
        internal set
    var value = value
        internal set
    var parent: Vertex_t? = null
        internal set
    var left: Vertex_t? = null
        internal set
    var right: Vertex_t? = null
        internal set

    operator fun compareTo(other: Vertex<K, V, Vertex_t>): Int {
        return key.compareTo(other.key)
    }

    fun toPair(): Pair<K, V> {
        return Pair(key, value)
    }
}
