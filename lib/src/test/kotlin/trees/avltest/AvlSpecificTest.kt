package trees.avltest

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import trees.implementations.AVLVertex
import trees.implementations.AvlTree
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class AvlSpecificTest {
    private lateinit var avlTree: AvlTree<Int, Int>

    companion object {
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
                "Height difference of subtrees must be in [-1,1]",
            )
            assertEquals(
                difference,
                vertex.diffHeight,
                "Property diffHeights must match real Height difference of subtrees (key = ${vertex.key})",
            )
            return maxOf(checkTreeInvariant(vertex.left), checkTreeInvariant(vertex.right)) + 1
        }
    }

    @BeforeEach
    fun setup() {
        avlTree = AvlTree()
    }

    @Test
    fun `remove with double rotate`() {
        val keys = intArrayOf(5, 0, 7, -1, 2, 6, 9, -2, 1, 3, 8, 4)
        for (key in keys) avlTree[key] = key
        avlTree.remove(7)

        val expectedResult = arrayOf(5, 0, null, -1, 2, 6, 9, -2, 1, 3, 8, 4)
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Tree must save all other vertices after remove that creates unbalance situation",
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
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Tree must save all other vertices after root remove",
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `rotate left, right diffHeight = -1`() {
        val keys = intArrayOf(0, -1, 2, 1, 3, 4)
        for (key in keys) avlTree[key] = key

        val expectedResult = Array<Int?>(keys.size) { i -> keys[i] }
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Get must return corresponding values after left rotate ( right.diffHeight = -1)",
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
    fun `rotate left, right diffHeight = 0`() {
        val keys = intArrayOf(0, -1, 4, 2, 6, -2, 1, 3, 5, 7)
        for (key in keys) avlTree[key] = key
        avlTree.remove(-2)

        val expectedResult = arrayOf(0, -1, 4, 2, 6, null, 1, 3, 5, 7)
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Get must return corresponding values after left rotate (right.diffHeight = 0)",
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

        val expectedResult = Array<Int?>(keys.size) { i -> keys[i] }
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Get must return corresponding values after right rotate (left.diffHeight = 1)",
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
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Get must return corresponding values after right rotate (left.diffHeight = 0)",
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

    @Test
    fun `rotate right-left, right diffHeight is 1, right-left diffHeight is 1`() {
        val keys = intArrayOf(1, 0, 4, 3, 5, 2)
        for (key in keys) avlTree[key] = key

        val expectedResult = Array<Int?>(keys.size) { i -> keys[i] }
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Get must return corresponding values after right-left rotate with right.left.diffHeight = 1",
        )
        checkTreeInvariant(avlTree.root)

//                1                    1                  3
//               / \                  / \                / \
//              0   4                0   3              1   4
//                 / \      -->         / \     -->    / \   \
//                3   5                2   4          0   2   5
//               /                          \
//              2                            5
    }

    @Test
    fun `rotate right-left, right diffHeight is 1, right-left diffHeight is 0`() {
        val keys = intArrayOf(2, 1, 6, 0, 4, 7, 3, 5)
        for (key in keys) avlTree[key] = key
        avlTree.remove(0)

        val expectedResult: Array<Int?> = arrayOf(2, 1, 6, null, 4, 7, 3, 5)
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Get must return corresponding values after right-left rotate with right.left.diffHeight = 0",
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `rotate right-left, right diffHeight is 1, right-left diffHeight is -1`() {
        val keys = intArrayOf(1, 0, 4, 2, 5, 3)
        for (key in keys) avlTree[key] = key

        val expectedResult = Array<Int?>(keys.size) { i -> keys[i] }
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Get must return corresponding values after right-left rotate with right.left.diffHeight = -1",
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `rotate left-right, left diffHeight is -1, left-right diffHeight is -1`() {
        val keys = intArrayOf(4, 1, 5, 0, 2, 3)
        for (key in keys) avlTree[key] = key

        val expectedResult = Array<Int?>(keys.size) { i -> keys[i] }
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Get must return corresponding values after left-right rotate with left.right.diffHeight = -1",
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `rotate left-right, left diffHeight is -1, left-right diffHeight is 0`() {
        val keys = intArrayOf(5, 1, 6, 0, 3, 7, 2, 4)
        for (key in keys) avlTree[key] = key
        avlTree.remove(7)

        val expectedResult = arrayOf(5, 1, 6, 0, 3, null, 2, 4)
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Get must return corresponding values after left-right rotate with left.right.diffHeight = 0",
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `rotate left-right, left diffHeight is -1, left-right diffHeight is 1`() {
        val keys = intArrayOf(4, 1, 5, 0, 3, 2)
        for (key in keys) avlTree[key] = key

        val expectedResult = Array<Int?>(keys.size) { i -> keys[i] }
        val actualResult = Array(keys.size) { i -> avlTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
            "Get must return corresponding values after left-right rotate with left.right.diffHeight = 1",
        )
        checkTreeInvariant(avlTree.root)
    }
}
