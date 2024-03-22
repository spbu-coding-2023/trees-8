package bstrees.implementations

import bstrees.templates.BSTreeTemplate

class SimpleTree<K : Comparable<K>, V> : BSTreeTemplate<K, V, SimpleVertex<K, V>>() {
    override fun remove(key: K): V? {
        return null
    }

    override operator fun set(key: K, value: V): V? {
        if (root == null){
            val vertex = SimpleVertex(key,value)
            root = vertex
            size+=1
            return null
        }

        var current = root ?: throw IllegalStateException()
        while (true){
            val result = current.key.compareTo(key)
            if (result < 0){
                if(current.left == null){
                    val newVertex = SimpleVertex(key, value)
                    current.left = newVertex
                    newVertex.parent = current
                    size+= 1
                    return null
                }
                current = current.left ?: throw IllegalStateException()
            } else if (result > 0){
                if (current.right == null){
                    val newVertex = SimpleVertex(key, value)
                    current.right = newVertex
                    newVertex.parent = current
                    size += 1
                    return null
                }
                current = current.right ?: throw IllegalStateException()
            } else{
                val oldValue = current.value
                current.value = value
                return null
            }
        }
    }
}