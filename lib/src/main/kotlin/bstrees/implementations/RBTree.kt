package bstrees.implementations

import bstrees.templates.BalanceBSTreeTemplate

class RBTree<K : Comparable<K>, V> : BalanceBSTreeTemplate<K, V, RBVertex<K, V>>() {
    override fun set(key: K, value: V): V? {
        val (currentVert, returnResult) = unbalancedSet(key, value)
        //balance(currentVert)
        return returnResult
    }
    private fun unbalancedSet(key: K, value: V): Pair<RBVertex<K, V>, V?> {
        if (root == null){
            val newVertex = RBVertex(key, value)
            root = newVertex
            size += 1
            return Pair(newVertex, null)
        }
        var currentVertex = root ?: throw IllegalStateException()
        while (true){
            val result = currentVertex.key.compareTo(key)
            if (result < 0){
                if(currentVertex.left == null){
                    val newVertex = RBVertex(key, value)
                    currentVertex.left = newVertex
                    newVertex.parent = currentVertex
                    size+= 1
                    return Pair(newVertex, null)
                }
                currentVertex = currentVertex.left ?: throw IllegalStateException()
            } else if (result > 0){
                if (currentVertex.right == null){
                    val newVertex = RBVertex(key, value)
                    currentVertex.right = newVertex
                    newVertex.parent = currentVertex
                    size += 1
                    return Pair(newVertex, null)
                }
                currentVertex = currentVertex.right ?: throw IllegalStateException()
            } else{
                val oldValue = currentVertex.value
                currentVertex.value = value
                return Pair(currentVertex, oldValue)
            }
        }
    }
    override fun remove(key: K): V?{
        return null
    }
}
