package trees.avltest

import trees.implementations.AVLVertex
import trees.implementations.AvlTree

class AvlTestTree<K : Comparable<K>, V> : AvlTree<K, V>() {
    fun getRootAvl(): AVLVertex<K, V>? {
        return this.root
    }
}