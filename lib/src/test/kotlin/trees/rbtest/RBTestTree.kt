package trees.rbtest

import trees.implementations.AVLVertex
import trees.implementations.AvlTree
import trees.implementations.RBTree
import trees.implementations.RBVertex

class RBTestTree<K : Comparable<K>, V> : RBTree<K, V>() {
    fun getRootRB(): RBVertex<K, V>? {
        return this.root
    }
}