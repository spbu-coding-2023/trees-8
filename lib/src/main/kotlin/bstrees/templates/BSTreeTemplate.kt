package bstrees.templates

import java.util.Stack

abstract class BSTreeTemplate<K : Comparable<K>, V, Vertex_t : VertexTemplate<K, V, Vertex_t>> {
    var root: Vertex_t? = null
    var size: Int = 0
        private set(value) {
            if (value >= 0) {
                field = value
            } else throw IllegalArgumentException("Tree size cannot be a negative number")
        }

    /**
     * Associates the specified value with the specified key in the tree.
     */
    public abstract operator fun set(key: K, value: V)

    /**
     * Returns the value corresponding to the given key, or null if such a key is not present in the tree.
     */
    public operator fun get(key: K): V? {
        var cur = root
        while (cur != null) {
            val result = cur.key.compareTo(key)
            when {
                result < 0 -> cur = cur.left
                result > 0 -> cur = cur.right
                result == 0 -> return cur.value
            }
        }
        return null
    }

    /**
     * Returns the value corresponding to the given key, or defaultValue if such a key is not present in the tree.
     */
    public fun getOrDefault(key: K, defaultValue: Any): Any {
        return get(key) ?: defaultValue
    }


    /**
     * Removes the entry for the specified key only if it is mapped with some value.
     * Returns: removed value if key was mapped with it, otherwise null
     */
    public abstract fun remove(key: K): V?

    public operator fun iterator(): TreeIterator {
        return TreeIterator()
    }

    inner class TreeIterator : Iterator<Pair<K, V>> {
        var cur = root
        val stack = Stack<Vertex_t>()
        override fun next(): Pair<K, V> {
            while (hasNext()) {
                if (cur != null) {
                    cur?.let {
                        stack.push(it)
                        cur = it.left
                    }
                } else {
                    val returnVert = stack.pop()
                    cur = returnVert.right
                    return returnVert.toPair()
                }
            }
            throw IndexOutOfBoundsException()
        }

        override fun hasNext(): Boolean {
            return stack.isNotEmpty() or (cur != null)
        }
    }

    /**
     * Returns Pair<K,V> by minimum key in the tree. If tree is empty then returns null
     */
    public fun min(): Pair<K, V>? {
        return minVertex(root)?.toPair()
    }

    protected fun minVertex(vertex: Vertex_t?): Vertex_t? {
        var cur = vertex
        while (cur?.left != null) {
            cur = cur.left
        }
        return cur
    }

    /**
     * Returns Pair<K,V> by maximum key in the tree. If tree is empty then returns null
     */
    public fun max(): Pair<K, V>? {
        return maxVert(root)?.toPair()
    }

    protected fun maxVert(vertex: Vertex_t?): Vertex_t? {
        var cur = vertex
        while (cur?.right != null) {
            cur = cur.right
        }
        return cur
    }

    fun clear() {
        size = 0
        root = null
    }

}