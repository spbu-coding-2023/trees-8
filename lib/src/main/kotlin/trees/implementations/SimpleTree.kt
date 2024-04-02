package trees.implementations

import trees.templates.BSTreeTemplate

class SimpleTree<K : Comparable<K>, V> : BSTreeTemplate<K, V, SimpleVertex<K, V>>() {

    override fun fabricVertex(key: K, value: V): SimpleVertex<K, V> {
        return SimpleVertex(key, value)
    }

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
            val successor = maxVertex(vertex.left)
            val successorParent = successor?.parent
            successor?.let {
                vertex.key = it.key
                vertex.value = it.value
            }
            when {
                successorParent?.left == successor -> successorParent?.left = null
                successorParent?.right == successor -> successorParent?.right = null
            }
        } else {
            val successor = minVertex(vertex.right)
            val successorParent = successor?.parent
            successor?.let {
                vertex.key = it.key
                vertex.value = it.value
            }
            when {
                successorParent?.left == successor -> successorParent?.left = null
                successorParent?.right == successor -> successorParent?.right = null
            }
        }
        --size
        return oldValue
    }

    override operator fun set(key: K, value: V): V? {
        val (currentVert, oldValue) = setWithoutBalance(key, value, ::fabricVertex)
        if (oldValue == null) {
            size += 1
        }
        return oldValue
    }

}
