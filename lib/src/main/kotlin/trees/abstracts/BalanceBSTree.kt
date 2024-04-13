package trees.abstracts

abstract class BalanceBSTree<K : Comparable<K>, V, Vertex_t : Vertex<K, V, Vertex_t>> :
    BSTree<K, V, Vertex_t>()
