package bstrees.implementations

import bstrees.templates.BalanceBSTreeTemplate

class RBTree<K : Comparable<K>, V> : BalanceBSTreeTemplate<K, V, RBVertex<K, V>>() {
    override fun set(key: K, value: V): V? {
        val (currentVert, returnResult) = unbalancedSet(key, value)
        balanceTreeAfterInsert(currentVert)
        return returnResult
    }

    private val red = RBVertex.Color.RED
    private val black = RBVertex.Color.BLACK

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
        } else if (parent.left == vertex) {
            parent.left = leftChild
        } else if (parent.right == vertex) {
            parent.right = leftChild
        }
        if (leftChild != null) {
            leftChild.parent = parent
        }
    }

    private fun rotateLeft(vertex: RBVertex<K, V>?) {
        val parent = vertex?.parent
        val rightChild = vertex?.right
        vertex?.right = rightChild?.left
        if (rightChild?.left != null) {
            rightChild.left?.parent = vertex
        }
        rightChild?.left = vertex
        vertex?.parent = rightChild
        if (parent == null) {
            root = rightChild
        } else if (parent.left == vertex) {
            parent.left = rightChild
        } else if (parent.right == vertex) {
            parent.right = rightChild
        }
        if (rightChild != null) {
            rightChild.parent = parent
        }
    }

    private fun unbalancedSet(key: K, value: V): Pair<RBVertex<K, V>, V?> {
        if (root == null) {
            val newVertex = RBVertex(key, value)
            root = newVertex
            size += 1
            return Pair(newVertex, null)
        }
        var currentVertex = root ?: throw IllegalStateException()
        while (true) {
            val result = currentVertex.key.compareTo(key)
            if (result > 0) {
                if (currentVertex.left == null) {
                    val newVertex = RBVertex(key, value)
                    newVertex.color = RBVertex.Color.RED
                    currentVertex.left = newVertex
                    newVertex.parent = currentVertex
                    size += 1
                    return Pair(newVertex, null)
                }
                currentVertex = currentVertex.left ?: throw IllegalStateException()
            } else if (result < 0) {
                if (currentVertex.right == null) {
                    val newVertex = RBVertex(key, value)
                    newVertex.color = RBVertex.Color.RED
                    currentVertex.right = newVertex
                    newVertex.parent = currentVertex
                    size += 1
                    return Pair(newVertex, null)
                }
                currentVertex = currentVertex.right ?: throw IllegalStateException()
            } else {
                val oldValue = currentVertex.value
                currentVertex.value = value
                return Pair(currentVertex, oldValue)
            }
        }
    }

    private fun balanceTreeAfterInsert(vertex: RBVertex<K, V>) {
        var parent = vertex.parent
        if (parent == null) {
            vertex.color = black
            return
        }
        if (parent.color == black) {
            return
        }
        val grandparent = parent.parent

        if (grandparent == null) {
            parent.color = black
            return
        }

        val uncle = getVertexUncle(parent)
        if (uncle != null && uncle.color == red) {
            parent.color = black
            grandparent.color = red
            uncle.color = black
            balanceTreeAfterInsert(grandparent)
        } else if (parent == grandparent.left) {
            if (vertex == parent.right) {
                rotateLeft(parent)
                parent = vertex
            }
            rotateRight(grandparent)
            parent.color = black
            grandparent.color = red
        } else {
            if (vertex == parent.left) {
                rotateRight(parent)
                parent = vertex
            }
            rotateLeft(grandparent)
            parent.color = black
            grandparent.color = red
        }
    }

    override fun remove(key: K): V? {
        var vertex = root
        while (vertex != null && vertex.key != key) {
            if (key < vertex.key) {
                vertex = vertex.left
            } else {
                vertex = vertex.right
            }
        }
        if (vertex == null) {
            return null
        }
        val replacedVertex: RBVertex<K, V>?
        val deletedVertexColor: RBVertex.Color
        if (vertex.left == null || vertex.right == null) {
            replacedVertex = deleteNullChild(vertex)
            deletedVertexColor = vertex.color
        } else {
            val minVertex = findMin(vertex.right!!)
            minVertex.color = vertex.color
            minVertex.left = vertex.left
            minVertex.right = vertex.right
            replaceChild(vertex.parent, vertex, minVertex)
            replacedVertex = deleteNullChild(minVertex)
            deletedVertexColor = minVertex.color
        }

        if (deletedVertexColor == RBVertex.Color.BLACK) {
            balanceTreeAfterDelete(replacedVertex)
            if (replacedVertex?.nullType == true) {
                replaceChild(replacedVertex.parent, replacedVertex, null)
            }
        }
        return replacedVertex?.value
    }

    private fun balanceTreeAfterDelete(vertex: RBVertex<K, V>?) {
        if (vertex == root) {
            vertex?.color = RBVertex.Color.BLACK
            return
        }
        var brother = getBrother(vertex)

        if (brother?.color === RBVertex.Color.RED) {
            manageRedBrother(vertex, brother)
            brother = getBrother(vertex)
        }
        if (brother?.left?.color == RBVertex.Color.BLACK && brother.right?.color == RBVertex.Color.BLACK) {
            brother.color = RBVertex.Color.RED
            if (vertex?.parent?.color == RBVertex.Color.RED) {
                vertex.parent?.color = RBVertex.Color.BLACK
            } else {
                balanceTreeAfterDelete(vertex?.parent)
            }
        } else {
            manageBlackRedOne(vertex, brother)
        }
    }

    private fun getBrother(vertex: RBVertex<K, V>?): RBVertex<K, V>? {
        val parent = vertex?.parent
        return if (vertex == parent?.left) {
            parent?.right
        } else if (vertex == parent?.right) {
            parent?.left
        } else {
            throw IllegalStateException()
        }
    }

    private fun manageBlackRedOne(vertex: RBVertex<K, V>?, argBrother: RBVertex<K, V>?) {
        var brother = argBrother
        if (vertex == vertex?.parent?.left && brother?.right?.color == black) {
            brother.left?.color = black
            brother.color = red
            rotateRight(brother)
            brother = vertex?.parent?.right
        } else if (vertex != vertex?.parent?.left && brother?.left?.color == black) {
            brother.right?.color = black
            brother.color = red
            rotateLeft(brother)
            brother = vertex?.parent?.left
        }
        brother?.color = vertex?.parent!!.color
        vertex.parent?.color = black
        if (vertex == vertex.parent?.left) {
            brother?.right?.color = black
            rotateLeft(vertex.parent)
        } else {
            brother?.left?.color = black
            rotateRight(vertex.parent)
        }
    }

    private fun manageRedBrother(vertex: RBVertex<K, V>?, brother: RBVertex<K, V>) {
        brother.color = RBVertex.Color.BLACK
        vertex?.parent?.color = RBVertex.Color.RED
        if (vertex === vertex?.parent?.left) {
            rotateLeft(vertex?.parent)
        } else {
            rotateRight(vertex?.parent)
        }
    }

    private fun replaceChild(parent: RBVertex<K, V>?, oldChild: RBVertex<K, V>, newChild: RBVertex<K, V>?) {
        if (parent == null) {
            root = newChild
        } else if (parent.left === oldChild) {
            parent.left = newChild
        } else if (parent.right === oldChild) {
            parent.right = newChild
        } else {
            throw IllegalStateException()
        }
        if (newChild != null) {
            newChild.parent = parent
        }
    }

    private fun deleteNullChild(vertex: RBVertex<K, V>): RBVertex<K, V>? {
        if (vertex.left != null) {
            replaceChild(vertex.parent, vertex, vertex.left)
            return vertex.left
        } else if (vertex.right != null) {
            replaceChild(vertex.parent, vertex, vertex.right)
            return vertex.right
        } else {
            var newChild: RBVertex<K, V>? = RBVertex(vertex.key, vertex.value)
            if (vertex.color == RBVertex.Color.BLACK) {
                newChild?.nullType = true
            } else {
                newChild = null
            }
            replaceChild(vertex.parent, vertex, newChild)
            return newChild
        }
    }

    private fun findMin(argVertex: RBVertex<K, V>): RBVertex<K, V> {
        var vertex = argVertex
        while (vertex.left != null) {
            vertex = vertex.left!!
        }
        return vertex
    }

    private fun getVertexUncle(parent: RBVertex<K, V>): RBVertex<K, V>? {
        val grandparent = parent.parent
        return if (grandparent?.left == parent) {
            grandparent.right
        } else if (grandparent?.right == parent) {
            grandparent.left
        } else {
            throw IllegalStateException("Grandparent/Parent dependency mistake")
        }
    }
}
