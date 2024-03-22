package bstrees.implementations

import bstrees.templates.BSTreeTemplate

class SimpleTree<K : Comparable<K>, V> : BSTreeTemplate<K, V, SimpleVertex<K, V>>() {
    override fun remove(key: K): V? {
        // remove implementation
        return null
    }

    override operator fun set(
        key: K,
        value: V,
    ): V? {
        // set implemenation
        return null
    }
}
