package bstrees.implementations

import bstrees.templates.BalanceBSTreeTemplate

class AVLTree<K : Comparable<K>, V> : BalanceBSTreeTemplate<K, V, AVLVertex<K, V>>() {

    public override operator fun set(key: K, value: V): V? {
        val (currentVert, returnResult) = setWithoutBalance(key, value)
        balanceAfterSet(currentVert)
        return returnResult
    }

    private fun setWithoutBalance(key: K, value: V): Pair<AVLVertex<K, V>, V?> {
        if (root == null) {
            val newVertex = AVLVertex(key, value)
            root = newVertex
            size += 1
            return Pair(newVertex, null)
        }

        var cur = root ?: throw IllegalStateException("Case when root is null is processed above")
        while (true) {
            val result = key.compareTo(cur.key)
            if (result < 0) {
                if (cur.left == null) {
                    val newVertex = AVLVertex(key, value)
                    cur.left = newVertex
                    newVertex.parent = cur
                    size += 1
                    return Pair(newVertex, null)
                }
                cur = cur.left ?: throw IllegalStateException("Case when cur.left is null is processed above")
            } else if (result > 0) {
                if (cur.right == null) {
                    val newVertex = AVLVertex(key, value)
                    cur.right = newVertex
                    newVertex.parent = cur
                    size += 1
                    return Pair(newVertex, null)
                }
                cur = cur.right ?: throw IllegalStateException("Case when cur.rightt is null is processed above")
            } else {
                val oldValue = cur.value
                cur.value = value
                return Pair(cur, oldValue)
            }
        }
    }

    private fun balanceAfterSet(vertex: AVLVertex<K, V>) {
        var cur = vertex
        var prevKey = cur.key
        while (cur.parent != null) {
            prevKey = cur.key
            cur = cur.parent ?: throw IllegalStateException("Parent can't be null due to while condition")
            if (prevKey == cur.left?.key) {
                cur.diffHeight += 1
            } else if (prevKey == cur.right?.key) {
                cur.diffHeight -= 1
            }

            cur = balanceOnce(cur)
            if (cur.diffHeight == 0) break
        }
    }

    public override fun remove(key: K): V? {
        var toRemove = VertByKey(key)
        var returnResult = toRemove?.value
        var toBalance: AVLVertex<K, V>? = null
        if (toRemove == null) return null

        if ((toRemove.left == null) and (toRemove.right == null)) {
            toBalance = toRemove.parent
            toRemove = null
        } else if (toRemove.right == null) {
            toRemove.left?.parent = toRemove.parent
            toRemove = toRemove.left
            toBalance = toRemove?.parent
        } else {
            var minRight = minVertex(toRemove.right)
            if (toRemove.right == minRight) {
            } else {
                minRight?.parent?.left = null
                minRight?.right = toRemove.right
            }
            minRight?.left = toRemove.left
            minRight?.parent = toRemove.parent
            toRemove = minRight
            toBalance = toRemove?.parent
        }
        return TODO()
    }

    private fun getDiffHeight(vertex: AVLVertex<K, V>?): Int {
        return vertex?.diffHeight ?: 0
    }

    private fun balanceOnce(vertex_: AVLVertex<K, V>): AVLVertex<K, V> {
        var vertex = vertex_
        if (vertex.diffHeight == 2) {
            if (getDiffHeight(vertex.left) >= 0) {
                vertex = rotateRight(vertex)
            } else if (getDiffHeight(vertex.left) == -1) {
                vertex.left?.let {
                    vertex = rotateLeft(it)
                    vertex = rotateRight(vertex)
                }
            }

        } else if (vertex.diffHeight == -2) {
            if (getDiffHeight(vertex.right) <= 0) {
                vertex = rotateLeft(vertex)
            } else if (getDiffHeight(vertex.right) == 1) {
                vertex.right?.let {
                    vertex = rotateRight(vertex)
                    vertex = rotateLeft(vertex)
                }
            }
        }
        return vertex
    }

    private fun rotateRight(origin: AVLVertex<K, V>): AVLVertex<K, V> {
        val left = origin.left
            ?: throw IllegalStateException("difference of heights can't be 2 if left Vertex doesn't exist")

        left.parent = origin.parent
        if (origin.parent?.left == origin) origin.parent?.left = left
        else origin.parent?.right = left

        origin.parent = left
        origin.left = left.right
        origin.left?.parent = origin
        left.right = origin

        if (left.diffHeight == 0) {
            origin.diffHeight = 1
            left.diffHeight = -1
        } else if (left.diffHeight == 1) {
            origin.diffHeight = 0
            left.diffHeight = 0
        }
        if (origin == root) {
            root = left
        }
        return left
    }

    private fun rotateLeft(origin: AVLVertex<K, V>): AVLVertex<K, V> {
        val right = origin.right
            ?: throw IllegalStateException("difference of heights can't be 2 if left Vertex doesn't exist")

        right.parent = origin.parent
        if (origin.parent?.left == origin) origin.parent?.left = right
        else origin.parent?.right = right

        origin.parent = right
        origin.right = right.left
        origin.right?.parent = origin
        right.left = origin

        if (right.diffHeight == 0) {
            origin.diffHeight = -1
            right.diffHeight = 1
        } else if (right.diffHeight == -1) {
            origin.diffHeight = 0
            right.diffHeight = 0
        }
        if (origin == root) {
            root = right
        }
        return right
    }
}
