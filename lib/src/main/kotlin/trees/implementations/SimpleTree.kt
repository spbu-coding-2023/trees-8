package trees.implementations

import trees.templates.BSTreeTemplate

class SimpleTree<K : Comparable<K>, V> : BSTreeTemplate<K, V, SimpleVertex<K, V>>() {
    override fun remove(key: K): V? {
        val vertex = vertByKey(key) ?: return null
        val parent = vertex.parent
        val oldValue = vertex.value
        if (vertex.left == null && vertex.right == null) {
            when {
                parent == null -> root = null
                parent.left == vertex -> parent.left = null
                else -> parent.right = null
            }
        } else if (vertex.right == null) {
            vertex.left?.let {
                vertex.key = it.key
                vertex.value = it.value
                vertex.left = null
            }
        } else {
            val successor = minVertex(vertex.right)
            val successorParent = successor?.parent
            successor?.let {
                vertex.key = successor.key
                vertex.value = successor.value
            }
            when {
                successorParent?.left == successor -> successorParent?.left = null
                else -> vertex.right = null
            }
        }
        --size
        return oldValue
    }

    override operator fun set(
        key: K,
        value: V,
    ): V? {
        if (root == null) {
            val vertex = SimpleVertex(key, value)
            root = vertex
            size += 1
            return null
        }

        var current = root ?: throw IllegalStateException("Can't be null, there was a check above")
        while (true) {
            val result = key.compareTo(current.key)
            if (result < 0) {
                if (current.left == null) {
                    val newVertex = SimpleVertex(key, value)
                    current.left = newVertex
                    newVertex.parent = current
                    size += 1
                    return null
                }
                current = current.left ?: throw IllegalStateException("Case when cur.left is null is processed above")
            } else if (result > 0) {
                if (current.right == null) {
                    val newVertex = SimpleVertex(key, value)
                    current.right = newVertex
                    newVertex.parent = current
                    size += 1
                    return null
                }
                current = current.right ?: throw IllegalStateException("Case when cur.right is null is processed above")
            } else {
                val oldValue = current.value
                current.value = value
                return null
            }
        }
    }
}
