package bstrees.implementations

import bstrees.templates.BSTreeTemplate

class SimpleBSTree<K : Comparable<K>, V> : BSTreeTemplate<K, V, SimpleVertex<K, V>>() {
    override fun remove(key: K): V? {
        // remove implementation
        return null
    }

    override operator fun set(key: K, value: V) {
        // set implemenation
    }
}