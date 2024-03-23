package bstrees.implementations

import bstrees.templates.BSTreeTemplate

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

    override operator fun set(key: K, value: V): V? {
        // set implemenation
        return null
    }
}