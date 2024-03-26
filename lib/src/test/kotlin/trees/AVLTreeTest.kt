/*
 * This Kotlin source file was generated by the Gradle 'init' task.
 */
package trees

import bstrees.implementations.AVLTree
import bstrees.implementations.AVLVertex
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class AVLTreeTest {
    private lateinit var avlTree: AVLTree<Int, Int>

    /**
     * Traverse all vertices of the tree and asserts that their diffHeights are
     * equal to real height difference of the subtrees.
     * @return height of the subtree with the root at the specified [vertex]
     */
    fun checkTreeInvariant(vertex: AVLVertex<*, *>?): Int {
        if (vertex == null) return 0

        val leftSubtreeHeight = checkTreeInvariant(vertex.left)
        val rightSubtreeHeight = checkTreeInvariant(vertex.right)
        val difference = leftSubtreeHeight - rightSubtreeHeight
        assertTrue(
            difference in -1..1,
            "Height difference of subtrees must be in [-1,1]"
        )
        assertEquals(
            difference, vertex.diffHeight,
            "Property diffHeights must match real Height difference of subtrees (key = ${vertex.key})"
        )
        return maxOf(checkTreeInvariant(vertex.left), checkTreeInvariant(vertex.right)) + 1
    }

    @BeforeEach
    fun setup() {
        avlTree = AVLTree<Int, Int>()
    }

    @Test
    fun `simple array test`() {
        val array = intArrayOf(0, 1, 2, 3, 4)
        for (i in array) {
            avlTree[i] = -i
        }
        val expectedGet: Array<Int?> = Array(5, { i -> -i })
        val actualGet: Array<Int?> = Array(5, { i -> avlTree.get(i) })
        assertContentEquals(expectedGet, actualGet, "Get method must return the value associated with the key")
        val expectedSize = 5
        val actualSize = avlTree.size
        assertEquals(expectedSize, actualSize, "Size the tree must correspond to the number key-value pairs")
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `size of avl tree with duplicate keys`() {
        for (i in 1..10) {
            avlTree[i] = i + 30
        }
        for (i in 6..10) {
            avlTree[i] = 0
        }

        val expectedSize = 10
        val actualSize = avlTree.size
        assertEquals(
            expectedSize, actualSize,
            "Size of the tree must not change after overwriting an existing key"
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `set keys twice`() {
        for (i in 1..10) {
            avlTree[i] = 30 + i
        }
        for (i in 1..10) {
            avlTree[i] = 0
        }

        val expectedSize = 10
        val actualSize = avlTree.size
        assertEquals(
            expectedSize, actualSize,
            "Size of the tree must not change after overwriting an existing key"
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `simple remove`() {
        avlTree[0] = 5

        val expectedResult = 5
        val actualResult = avlTree.remove(0)
        val expectedSize = 0
        val actualSize = avlTree.size

        assertEquals(
            expectedResult, actualResult,
            "Remove must return removed value"
        )
        assertEquals(
            expectedSize, actualSize,
            "Size of the tree must decrease after removing the existing key"
        )
    }

    @Test
    fun `remove with left vertex`() {
        val keys = intArrayOf(0, -1, 1, -2)
        for (key in keys) avlTree[key] = key
        avlTree.remove(-1)

        val expectedResult = arrayOf(0, null, 1, -2)
        val actualResult = Array<Int?>(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult, actualResult,
            "Tree must save all other vertices after remove vertex with existing left vertex"
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `remove with right subtree of height 1`() {
        val keys = intArrayOf(0, -1, 3, 1, 2)
        for (key in keys) avlTree[key] = key
        avlTree.remove(3)

        val expectedResult = arrayOf(0, -1, null, 1, 2)
        val actualResult = Array<Int?>(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult, actualResult,
            "Tree must save all other vertices after remove vertex with right subtree of height 1"
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `remove with right subtree of height more than 1`() {
        val keys = intArrayOf(0, -1, 3, -2, 1, 6, 4, 7)
        for (key in keys) avlTree[key] = key
        avlTree.remove(3)

        val expectedResult = arrayOf(0, -1, null, -2, 1, 6, 4, 7)
        val actualResult = Array<Int?>(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult, actualResult,
            "Tree must save all other vertices after remove vertex with right subtree of height >1"
        )
        checkTreeInvariant(avlTree.root)
        avlTree[5] = 5
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `remove with necessary double rotate`() {
        val keys = intArrayOf(5, 0, 7, -1, 2, 6, 9, -2, 1, 3, 8, 4)
        for (key in keys) avlTree[key] = key
        avlTree.remove(7)

        val expectedResult = arrayOf(5, 0, null, -1, 2, 6, 9, -2, 1, 3, 8, 4)
        val actualResult = Array<Int?>(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult, actualResult,
            "Tree must save all other vertices after remove that creates unbalance situation"
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `remove root with double rotate`() {
        val keys = intArrayOf(4, 1, 6, 0, 3, 5, 2)
        for (key in keys) avlTree[key] = key
        avlTree.remove(4)

        assertTrue(avlTree.root != null)
        val expectedResult = arrayOf(null, 1, 6, 0, 3, 5, 2)
        val actualResult = Array<Int?>(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult, actualResult,
            "Tree must save all other vertices after root remove"
        )
        checkTreeInvariant(avlTree.root)

    }

    @Test
    fun `remove a non-existent key`() {
        avlTree[0] = 15
        avlTree.remove(0)

        val expectedResult = null
        val actualResult = avlTree.remove(0)
        assertEquals(
            expectedResult, actualResult,
            "Remove a non-existent key must return null"
        )
    }

    @Test
    fun `simple iteration`() {
        val keys = intArrayOf(1, 7, 4, 9, 2, -44, 3)
        val values = intArrayOf(19, 14, 31, 17, 12, -34, 0)
        for (i in 0..<keys.size) {
            avlTree[keys[i]] = values[i]
        }

        val expectedResult: Array<Int?> = arrayOf(-34, 19, 12, 0, 31, 14, 17)
        val iterator = avlTree.iterator()
        val actualResult: Array<Int?> = Array(7, { iterator.next().second })
        assertContentEquals(expectedResult, actualResult, "Iterator must return vertices in keys order")
        checkTreeInvariant(avlTree.root)

    }

    @Test
    fun `rotate left, right diffHeight = -1`() {
        val keys = intArrayOf(0, -1, 2, 1, 3, 4)
        for (key in keys) avlTree[key] = key

        val expectedResult = Array<Int?>(keys.size, { i -> keys[i] })
        val actualResult = Array<Int?>(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult, actualResult,
            "Get must return corresponding values after left rotate ( right.diffHeight = -1)"
        )
        checkTreeInvariant(avlTree.root)

//                  0                    0                    2
//                 / \                  / \                 /   \
//               -1   2               -1   2               0     3
//                   / \      -->         / \     -->     / \     \
//                  1   3                1   3          -1   1     4
//                                            \
//                                             4
    }

    @Test
    fun `rotate left, right diffHeiht = 0`() {
        val keys = intArrayOf(0, -1, 4, 2, 6, -2, 1, 3, 5, 7)
        for (key in keys) avlTree[key] = key
        avlTree.remove(-2)

        val expectedResult = arrayOf(0, -1, 4, 2, 6, null, 1, 3, 5, 7)
        val actualResult = Array<Int?>(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult, actualResult,
            "Get must return corresponding values after left rotate (right.diffHeight = 0)"
        )
        checkTreeInvariant(avlTree.root)
//                  0                     0                      4
//                 / \                   / \                    /  \
//               -1   4                -1   4                  0    6
//              /    /  \       -->        /  \      -->      / \  / \
//            -2    2    6                2    6            -1  2  5  7
//                 / \  / \              / \  / \              / \
//                1  3  5  7            1  3  5  7            1   3
    }

    @Test
    fun `rotate right, left diffHeight = 1`() {
        val keys = intArrayOf(0, 1, -1, -3, -2, -4)
        for (key in keys) avlTree[key] = key

        val expectedResult = Array<Int?>(keys.size, { i -> keys[i] })
        val actualResult = Array<Int?>(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult, actualResult,
            "Get must return corresponding values after right rotate (left.diffHeight = 1)"
        )
        checkTreeInvariant(avlTree.root)

//                  0                  0                  -1
//                 / \                / \                /  \
//               -1   1             -1   1             -3    0
//              /  \       -->     /  \       -->     /     /  \
//            -3   -2            -3   -2             -4    -2   1
//                               /
//                             -4
    }

    @Test
    fun `rotate right, left diffHeight = 0`() {
        val keys = intArrayOf(7, 3, 8, 9, 1, 5, 0, 2, 4, 6)
        for (key in keys) avlTree[key] = key
        avlTree.remove(9)

        val expectedResult = arrayOf(7, 3, 8, null, 1, 5, 0, 2, 4, 6)
        val actualResult = Array(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult, actualResult,
            "Get must return corresponding values after right rotate (left.diffHeight = 0)"
        )
        checkTreeInvariant(avlTree.root)

//                 7                     7                        3
//                / \                   / \                      /  \
//               3   8                 3   8                    1    7
//              / \   \       -->     / \          -->         / \  / \
//             1   5   9             1   5                    0  2  5  8
//            / \ / \               / \ / \                        / \
//           0  2 4  6             0  2 4  6                      4   6
    }
}
