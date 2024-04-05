package trees.bstest

import trees.implementations.SimpleTree
import trees.implementations.SimpleVertex

class SimpleTestTree<K : Comparable<K>, V> : SimpleTree<K, V>() {
    fun getRootSimple(): SimpleVertex<K, V>? {
        return this.root
    }
}