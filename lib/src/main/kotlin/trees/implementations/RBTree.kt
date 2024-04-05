package trees.implementations

import trees.templates.BalanceBSTreeTemplate

open class RBTree<K : Comparable<K>, V> : BalanceBSTreeTemplate<K, V, RBVertex<K, V>>() {

    override fun fabricVertex(key: K, value: V): RBVertex<K, V> {
        return RBVertex(key, value)
    }

    override fun set(key: K, value: V): V? {
        val (currentVert, oldValue) = setWithoutBalance(key, value, ::fabricVertex)
        if (oldValue == null) {
            size += 1
        }
        if (currentVert == root) {
            currentVert.color = black
        }
        balanceTreeAfterInsert(currentVert)
        return oldValue
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
        val vertex = vertByKey(key) ?: return null

        size -= 1
        val replacedVertex: RBVertex<K, V>?
        val deletedVertexColor: RBVertex.Color
        if (vertex.left == null || vertex.right == null) {
            replacedVertex = deleteNullChild(vertex)
            deletedVertexColor = vertex.color
        } else {
            val mVertex = minVertex(vertex.right!!)!!
            vertex.value = mVertex.value
            vertex.key = mVertex.key
            replacedVertex = deleteNullChild(mVertex)
            deletedVertexColor = mVertex.color

        }

        if (deletedVertexColor == black) {
            balanceTreeAfterDelete(replacedVertex)
            if (replacedVertex?.additionalType == true) {
                replaceChild(replacedVertex.parent, replacedVertex, null)
            }
        }
        return replacedVertex?.value
    }

    private fun balanceTreeAfterDelete(vertex: RBVertex<K, V>?) {
        if (vertex == root) {
            vertex?.color = black
            return
        }
        var brother = getBrother(vertex)
        if (brother?.color == red) {
            manageRedBrother(vertex, brother)
            brother = getBrother(vertex)
        }
        if (brother?.left?.color == black && brother.right?.color == black) {
            brother.color = red
            if (vertex?.parent?.color == red) {
                vertex.parent?.color = black
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
        val isParent = vertex == vertex?.parent?.left
        if (isParent && brother?.right?.color == black) {
            brother.left?.color = black
            brother.color = red
            rotateLeft(brother)
            brother = vertex?.parent?.right

        } else if (!isParent && brother?.left?.color == black) {
            brother.right?.color = black
            brother.color = red
            rotateRight(brother)
            brother = vertex?.parent?.left
        }
        brother?.color = vertex?.parent!!.color
        vertex.parent?.color = black
    }

    private fun manageRedBrother(vertex: RBVertex<K, V>?, brother: RBVertex<K, V>) {
        brother.color = black
        vertex?.parent?.color = red
        if (vertex === vertex?.parent?.left) {
            rotateLeft(vertex?.parent)
        } else {
            rotateRight(vertex?.parent)
        }
    }

    private fun replaceChild(parent: RBVertex<K, V>?, oldChild: RBVertex<K, V>, newChild: RBVertex<K, V>?) {
        if (parent == null) {
            root = newChild
        } else if (parent.left == oldChild) {
            parent.left = newChild
        } else if (parent.right == oldChild) {
            parent.right = newChild
        } else {
            throw IllegalStateException()
        }
        if (newChild != null) {
            newChild.parent = parent
        }
    }

    private fun deleteNullChild(vertex: RBVertex<K, V>): RBVertex<K, V>? {
        println(vertex.key)
        if (vertex.left != null) {
            replaceChild(vertex.parent, vertex, vertex.left)
            return vertex.left
        } else if (vertex.right != null) {
            replaceChild(vertex.parent, vertex, vertex.right)
            return vertex.right
        } else {
            var newChild: RBVertex<K, V>? = RBVertex(vertex.key, vertex.value)
            if (vertex.color == black) {
                newChild?.additionalType = true
                newChild?.color = black
            } else {
                newChild = null
            }
            replaceChild(vertex.parent, vertex, newChild)
            return newChild
        }
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
