package bstrees.implementations

import bstrees.templates.VertexTemplate

class SimpleVertex<K: Comparable<K>,V>(key: K,value: V): VertexTemplate<K,V,SimpleVertex<K,V>>(key,value)

class AVLVertex<K: Comparable<K>,V>(key: K,value: V): VertexTemplate<K,V,AVLVertex<K,V>>(key,value){
    var height: Int = 0
}
class RBVertex<K: Comparable<K>,V>(key:K,value:V): VertexTemplate<K,V,RBVertex<K,V>>(key,value){
    var color : Color = Color.BLACK

    enum class Color{
        BLACK,
        RED
    }
}