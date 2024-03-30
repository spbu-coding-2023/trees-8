package bstrees.templates

import java.util.Stack

                                                                                                                        
abstract class BSTreeTemplate<K : Comparable<K>, V, Vertex_t : VertexTemplate<K, V, Vertex_t>> {
    var root: Vertex_t? = null
        protected set
    var size: Int = 0
        protected set(value) {
            if (value >= 0) {
                field = value
            } else {
                throw IllegalArgumentException("Tree size cannot be a negative number")
            }
        }

    /**
     * Associates the specified [value] with the specified [key] in the tree.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the tree.
     */
    public abstract operator fun set(
        key: K,
        value: V,
    ): V?

    /**
     * Associates the specified value with the specified key if this key is not in the tree.
     * @return [true] if this key is not in the tree, otherwise [false]
     */
    public fun setIfAbsent(
        key: K,
        value: V,
    ): Boolean {
        if (get(key) == null) {
            set(key, value)
            return true
        }
        return false
    }

    /**
     * Returns the value corresponding to the given [key], or `null` if such a key is not present in the tree.
     */
    public operator fun get(key: K): V? {
        return vertByKey(key)?.value
    }

    /**
     * Returns the value corresponding to the given [key], or [defaultValue] if such a key is not present in the map.
     */
    public fun getOrDefault(
        key: K,
        defaultValue: Any,
    ): Any {
        return get(key) ?: defaultValue
    }

    /**
     * Returns the value for the given [key] if the value is present and not `null`.
     * Otherwise, calls the [set] function,
     * set [defaultValue] into the tree under the given key and returns the call result.
     */
    public fun getOrSet(
        key: K,
        defaultValue: V,
    ): V? {
        return get(key) ?: set(key, defaultValue)
    }

    /**
     * Removes the specified key and its corresponding value from the tree.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the map.
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

    private fun maxVert(vertex: Vertex_t?): Vertex_t? {
        var cur = vertex
        while (cur?.right != null) {
            cur = cur.right
        }
        return cur
    }

    protected fun vertByKey(key: K): Vertex_t? {
        var cur = root
        while (cur != null) {
            if (key < cur.key) {
                cur = cur.left
            } else if (key > cur.key) {
                cur = cur.right
            } else if (key == cur.key) {
                break
            }
        }
        return cur
    }

    public fun isEmpty(): Boolean {
        return size == 0
    }

    public fun isNotEmpty(): Boolean {
        return size != 0
    }

    public fun containsKey(key: K): Boolean {
        return get(key) != null
    }

    public fun clear() {
        size = 0
        root = null
    }
}
