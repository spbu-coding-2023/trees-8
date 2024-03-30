package bstrees.implementations

import bstrees.templates.BalanceBSTreeTemplate
                                                                                                    
class AVLTree<K : Comparable<K>, V> : BalanceBSTreeTemplate<K, V, AVLVertex<K, V>>() {
    public override operator fun set(
        key: K,
        value: V,
    ): V? {
        val (currentVert, oldValue) = setWithoutBalance(key, value)
        if (oldValue == null) {
            size += 1
            balanceAfterSet(currentVert)
        }
        return oldValue
    }

    /**
     * Set specified value by specified key
     *
     * Returns: a pair of set vertex and old value.
     * If key didn't exist, the returned value is null.
     */
    private fun setWithoutBalance(
        key: K,
        value: V,
    ): Pair<AVLVertex<K, V>, V?> {
        if (root == null) {
            val newVertex = AVLVertex(key, value)
            root = newVertex
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
                    return Pair(newVertex, null)
                }
                cur = cur.left ?: throw IllegalStateException("Case when cur.left is null is processed above")
            } else if (result > 0) {
                if (cur.right == null) {
                    val newVertex = AVLVertex(key, value)
                    cur.right = newVertex
                    newVertex.parent = cur
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

    /**
     * Climbing up the tree, updates diffHeights of vertices after set and calls
     * [balanceOnce] if vertex became unbalanced
     */
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
        val toRemove = vertByKey(key) ?: return null
        val oldValue = toRemove.value
        removeVert(toRemove)
        size -= 1
        return oldValue
    }

    /**
     * Removes specified vertex and balances the tree
     */
    private fun removeVert(toRemove: AVLVertex<K, V>) {
        val parent = toRemove.parent
        if (toRemove.left == null && toRemove.right == null) {
            when {
                parent == null -> root = null

                parent.left == toRemove -> {
                    parent.left = null
                    parent.diffHeight -= 1
                }

                parent.right == toRemove -> {
                    parent.right = null
                    parent.diffHeight += 1
                }
            }
            balanceAfterRemove(parent)
        } else if (toRemove.right == null) {
            toRemove.left?.let {
                toRemove.key = it.key
                toRemove.value = it.value
                toRemove.left = null
                toRemove.diffHeight = 0
            }
            balanceAfterRemove(parent)
            when {
                parent?.left == toRemove -> parent.diffHeight -= 1
                parent?.right == toRemove -> parent.diffHeight += 1
            }
        } else {
            val minRight =
                minVertex(toRemove.right)
                    ?: throw IllegalStateException("min of subtree can't be null if it's contains at least one element")
            toRemove.key = minRight.key
            toRemove.value = minRight.value
            removeVert(minRight)
        }
    }

    /**
     * Climbing up the tree, updates diffHeights of vertices after remove and calls
     * [balanceOnce] if vertex became unbalanced
     */
    private fun balanceAfterRemove(vertex: AVLVertex<K, V>?) {
        var cur = vertex ?: return
        while (cur.diffHeight != 1 && cur.diffHeight != -1) {
            cur = balanceOnce(cur)
            if (cur.diffHeight != 1 && cur.diffHeight != -1) {
                when {
                    cur.parent?.left == cur -> cur.parent?.let { it.diffHeight -= 1 }
                    cur.parent?.right == cur -> cur.parent?.let { it.diffHeight += 1 }
                }
            }
            cur = cur.parent ?: return
        }
    }

    private fun getDiffHeight(vertex: AVLVertex<K, V>?): Int {
        return vertex?.diffHeight ?: 0
    }

    /**
     * Balances subtree by specified root, updates diffHeights of vertices
     * @return new root of subtree
     */
    private fun balanceOnce(_vertex: AVLVertex<K, V>): AVLVertex<K, V> {
        var vertex = _vertex
        if (vertex.diffHeight == 2) {
            if (getDiffHeight(vertex.left) >= 0) {
                vertex = rotateRight(vertex)
            } else if (getDiffHeight(vertex.left) == -1) {
                vertex.left?.let {
                    vertex.left = rotateLeft(it)
                    vertex = rotateRight(vertex)
                }
            }
        } else if (vertex.diffHeight == -2) {
            if (getDiffHeight(vertex.right) <= 0) {
                vertex = rotateLeft(vertex)
            } else if (getDiffHeight(vertex.right) == 1) {
                vertex.right?.let {
                    rotateRight(it)
                    vertex = rotateLeft(vertex)
                }
            }
        }
        return vertex
    }

    private fun rotateRight(origin: AVLVertex<K, V>): AVLVertex<K, V> {
        val left =
            origin.left
                ?: throw IllegalStateException("Height difference can't be 2 if left Vertex doesn't exist")

        left.parent = origin.parent
        if (origin.parent?.left == origin) {
            origin.parent?.left = left
        } else {
            origin.parent?.right = left
        }

        origin.parent = left
        origin.left = left.right
        origin.left?.parent = origin
        left.right = origin

        when (origin.diffHeight) {
            2 -> {
                when (left.diffHeight) {
                    0 -> {
                        origin.diffHeight = 1
                        left.diffHeight = -1
                    }

                    1 -> {
                        origin.diffHeight = 0
                        left.diffHeight = 0
                    }

                    2 -> {
                        origin.diffHeight = -1
                        left.diffHeight = 0
                    }
                }
            }

            1 -> {
                when (left.diffHeight) {
                    0 -> {
                        origin.diffHeight = 0
                        left.diffHeight = -1
                    }

                    1 -> {
                        origin.diffHeight = -1
                        left.diffHeight = -1
                    }

                    -1 -> {
                        origin.diffHeight = 0
                        left.diffHeight = -2
                    }
                }
            }
        }

        if (origin == root) {
            root = left
        }
        return left
    }

    private fun rotateLeft(origin: AVLVertex<K, V>): AVLVertex<K, V> {
        val right =
            origin.right
                ?: throw IllegalStateException("Height difference can't be 2 if left Vertex doesn't exist")

        right.parent = origin.parent
        if (origin.parent?.left == origin) {
            origin.parent?.left = right
        } else {
            origin.parent?.right = right
        }

        origin.parent = right
        origin.right = right.left
        origin.right?.parent = origin
        right.left = origin

        when (origin.diffHeight) {
            -2 -> {
                when (right.diffHeight) {
                    0 -> {
                        origin.diffHeight = -1
                        right.diffHeight = 1
                    }

                    -1 -> {
                        origin.diffHeight = 0
                        right.diffHeight = 0
                    }

                    -2 -> {
                        origin.diffHeight = 1
                        right.diffHeight = 0
                    }
                }
            }

            -1 -> {
                when (right.diffHeight) {
                    0 -> {
                        origin.diffHeight = 0
                        right.diffHeight = 1
                    }

                    -1 -> {
                        origin.diffHeight = 1
                        right.diffHeight = 1
                    }

                    1 -> {
                        origin.diffHeight = 0
                        right.diffHeight = 2
                    }
                }
            }
        }

        if (origin == root) {
            root = right
        }
        return right
    }
}
