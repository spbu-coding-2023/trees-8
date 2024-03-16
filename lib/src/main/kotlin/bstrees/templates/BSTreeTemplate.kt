package bstrees.templates

abstract class BSTreeTemplate<K : Comparable<K>, V, Vertex_t : VertexTemplate<K, V, Vertex_t>> {
    var root: Vertex_t? = null
    var size: Int = 0
        private set(value) {
            if (value >= 0) {
                field = value
            } else throw IllegalArgumentException("Tree size cannot be a negative number")
        }

    /**
     * Returns the value corresponding to the given key, or null if such a key is not present in the tree.
     */
    operator fun get(key: K): V? {
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
     * Associates the specified value with the specified key in the tree.
     */
    public abstract operator fun set(key: K, value: V)

    /**
     * Removes the entry for the specified key only if it is mapped with some value.
     * Returns: removed value if key was mapped with it, otherwise null
     */
    public abstract fun remove(key: K): V?

    public fun clear() {
        size = 0
        root = null
    }

    enum class Traversal {
        INORDER,
        PREORDER,
        POSTORDER
    }

    /**
     * Returns a [MutableList] of all values in this tree.
     */
    fun values(order: Traversal): List<V> {
        var result: MutableList<V> = mutableListOf()
        traverse(order, root, result)
        return result
    }


    private fun traverse(order: Traversal, vertex: Vertex_t?, result: MutableList<V>) {
        if (vertex == null) {
            return
        }
        when (order) {
            Traversal.INORDER -> {
                traverse(order, vertex.left, result)
                result.add(vertex.value)
                traverse(order, vertex.right, result)
            }

            Traversal.PREORDER -> {
                result.add(vertex.value)
                traverse(order, vertex.left, result)
                traverse(order, vertex.right, result)
            }

            Traversal.POSTORDER -> {
                traverse(order, vertex.left, result)
                traverse(order, vertex.right, result)
                result.add(vertex.value)
            }
        }
        return
    }

}