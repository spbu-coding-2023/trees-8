package bstrees.implementations

import bstrees.templates.BSTreeTemplate

class SimpleTree<K : Comparable<K>, V> : BSTreeTemplate<K, V, SimpleVertex<K, V>>() {
    override fun remove(key: K): V? {
        if (containsKey(key)) {
            val vertex = VertByKey(key)
            val parent = vertex?.parent
            if (vertex?.left == null && vertex?.right == null) {
                when {
                    parent == null -> root = null
                    parent.left == vertex -> parent.left = null
                    else -> parent.right = null
                }
            }
            else if (vertex.left != null && vertex.right == null) {
                when {
                    parent == null -> {
                        root = vertex.left
                        root?.parent = null
                    }
                    parent.left == vertex -> {
                        parent.left = vertex.left
                        vertex.left?.parent = parent
                    }
                    else -> {
                        parent.right = vertex.left
                        vertex.left?.parent = parent
                    }
                }
            }
            else if (vertex.left == null) {
                when {
                    parent == null -> {
                        root = vertex.right
                        root?.parent = null
                    }
                    parent.left == vertex -> {
                        parent.left = vertex.right
                        vertex.right?.parent = parent
                    }
                    else -> {
                        parent.right = vertex.right
                        vertex.right?.parent = parent
                    }
                }
            }
            else {
                val successor = minVertex(vertex.right)
                val successorParent = successor?.parent

                vertex.key = successor?.key ?: vertex.key
                vertex.value = successor?.value ?: vertex.value

                when {
                    successorParent?.left == successor -> successorParent?.left = successor?.right
                    else -> successorParent?.right = successor?.right
                }

                successor?.right?.parent = successorParent
            }

            size--
            return vertex?.value
        }
        return null
    }


    override operator fun set(key: K, value: V): V? {
        // set implemenation
        return null
    }
}