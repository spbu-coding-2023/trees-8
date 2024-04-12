package trees.templates

import java.util.Stack

abstract class BSTreeTemplate<K : Comparable<K>, V, Vertex_t : VertexTemplate<K, V, Vertex_t>> {
    internal var root: Vertex_t? = null
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
    abstract operator fun set(key: K, value: V): V?

    abstract protected fun createVertex(key: K, value: V): Vertex_t

    /**
     * Set specified value by specified key
     *
     * Returns: a pair of set vertex and old value.
     * If key didn't exist, the returned value is null.
     */
    protected fun setWithoutBalance(key: K, value: V): Pair<Vertex_t, V?> {
        if (root == null) {
            val newVertex = createVertex(key, value)
            root = newVertex
            return Pair(newVertex, null)
        }

        var cur = root ?: throw IllegalStateException("Case when root is null is processed above")
        while (true) {
            val result = key.compareTo(cur.key)
            if (result < 0) {
                if (cur.left == null) {
                    val newVertex = createVertex(key, value)
                    cur.left = newVertex
                    newVertex.parent = cur
                    return Pair(newVertex, null)
                }
                cur = cur.left ?: throw IllegalStateException("Case when cur.left is null is processed above")
            } else if (result > 0) {
                if (cur.right == null) {
                    val newVertex = createVertex(key, value)
                    cur.right = newVertex
                    newVertex.parent = cur
                    return Pair(newVertex, null)
                }
                cur = cur.right ?: throw IllegalStateException("Case when cur.right is null is processed above")
            } else {
                val oldValue = cur.value
                cur.value = value
                return Pair(cur, oldValue)
            }
        }
    }

    /**
     * Associates the specified value with the specified key if this key is not in the tree.
     * @return true if this key is not in the tree, otherwise false
     */
    fun setIfAbsent(key: K, value: V): Boolean {
        if (get(key) == null) {
            set(key, value)
            return true
        }
        return false
    }

    /**
     * Returns the value corresponding to the given [key], or `null` if such a key is not present in the tree.
     */
    operator fun get(key: K): V? {
        return vertByKey(key)?.value
    }

    /**
     * Returns the value corresponding to the given [key], or [defaultValue] if such a key is not present in the map.
     */
    fun getOrDefault(key: K, defaultValue: Any): Any {
        return get(key) ?: defaultValue
    }

    /**
     * Returns the value for the given [key] if the value is present and not `null`.
     * Otherwise, calls the [set] function,
     * set [defaultValue] into the tree under the given key and returns null.
     */
    fun getOrSet(key: K, defaultValue: V): V? {
        return get(key) ?: set(key, defaultValue)
    }

    /**
     * Removes the specified key and its corresponding value from the tree.
     *
     * @return the previous value associated with the key, or `null` if the key was not present in the map.
     */
    abstract fun remove(key: K): V?

    operator fun iterator(): TreeIterator {
        return TreeIterator()
    }

    inner class TreeIterator : Iterator<Pair<K, V>> {
        private var cur = root
        private val stack = Stack<Vertex_t>()

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
    fun min(): Pair<K, V>? {
        return minVertex(root)?.toPair()
    }

    protected fun minVertex(vertex: Vertex_t?): Vertex_t? {
        var cur = vertex
        while (cur?.left != null) {
            cur = cur.left
        }
        return cur
    }

    protected fun maxVertex(vertex: Vertex_t?): Vertex_t? {
        var cur = vertex
        while (cur?.right != null) {
            cur = cur.right
        }
        return cur
    }

    /**
     * Returns Pair<K,V> by maximum key in the tree. If tree is empty then returns null
     */
    fun max(): Pair<K, V>? {
        return maxVertex(root)?.toPair()
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

    fun isEmpty(): Boolean {
        return size == 0
    }

    fun isNotEmpty(): Boolean {
        return size != 0
    }

    fun containsKey(key: K): Boolean {
        return get(key) != null
    }

    fun clear() {
        size = 0
        root = null
    }
}
