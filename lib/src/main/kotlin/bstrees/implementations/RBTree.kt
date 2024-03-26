package bstrees.implementations

import bstrees.templates.BalanceBSTreeTemplate

class RBTree<K : Comparable<K>, V> : BalanceBSTreeTemplate<K, V, RBVertex<K, V>>() {
    override fun set(key: K, value: V): V? {
        val (currentVert, returnResult) = unbalancedSet(key, value)
        balanceTreeAfterInsert(currentVert)
        return returnResult
    }
    private fun rotateRight(vertex: RBVertex<K, V>?) {
        val parent = vertex?.parent
        val leftChild = vertex?.left
        vertex?.left = leftChild?.right
        if (leftChild?.right != null) {
            leftChild.right?.parent = vertex
        }
        leftChild?.right = vertex
        vertex?.parent = leftChild
        if (parent == null) {
            root = leftChild
        }
        else if (parent.left == vertex) {
            parent.left = leftChild
        }
        else if (parent.right == vertex) {
            parent.right = leftChild
        }
        if (leftChild != null) {
            leftChild.parent = parent
        }
    }
    private fun rotateLeft(vertex: RBVertex<K, V>?) {
        val parent  = vertex?.parent
        val rightChild = vertex?.right
        vertex?.right = rightChild?.left
        if (rightChild?.left != null) {
            rightChild.left?.parent = vertex
        }
        rightChild?.left = vertex
        vertex?.parent = rightChild
        if (parent == null) {
            root = rightChild
        }
        else if (parent.left == vertex) {
            parent.left = rightChild
        } else if (parent.right == vertex) {
            parent.right = rightChild
        }
        if (rightChild != null) {
            rightChild.parent = parent
        }
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
            if (result > 0){
                if(currentVertex.left == null){
                    val newVertex = RBVertex(key, value)
                    currentVertex.left = newVertex
                    newVertex.parent = currentVertex
                    size+= 1
                    return Pair(newVertex, null)
                }
                currentVertex = currentVertex.left ?: throw IllegalStateException()
            } else if (result < 0){
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

    private fun balanceTreeAfterInsert(vertex: RBVertex<K, V>){
        var parent = vertex.parent
        val black = RBVertex.Color.BLACK
        val red = RBVertex.Color.RED

        val grandparent = parent?.parent
        if (grandparent == null) {
            parent?.color = black
            return
        }
        if (parent == null) {
            vertex.color = black
            return
        }
        val uncle = getVertexUncle(parent)
        if (uncle != null && uncle.color == red) {
            parent.color = black
            grandparent.color = red
            uncle.color = black
            balanceTreeAfterInsert(grandparent)
        }
        else if (parent == grandparent.left) {
            if (vertex == parent.right) {
                rotateLeft(parent)
                parent = vertex
            }
            rotateRight(grandparent)
            parent.color = black
            grandparent.color = red
        }
        else {
            if (vertex == parent.left) {
                rotateRight(parent)
                parent = vertex
            }
            rotateLeft(grandparent)
            grandparent.color = red

            parent.color = black
        }
    }

    private fun getVertexUncle(parent: RBVertex<K, V>?): RBVertex<K, V>? {
        val grandparent = parent?.parent
        return if (grandparent?.right == parent) {
            grandparent?.right
        } else if (grandparent?.left == parent) {
            grandparent?.left
        } else {
            throw IllegalStateException("Grandparent/Parent dependency mistake")
        }
    }
    override fun remove(key: K): V?{
        return null
    }
}