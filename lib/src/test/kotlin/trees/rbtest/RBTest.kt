package trees.avlTest

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import trees.implementations.RBTree
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import trees.implementations.RBVertex
import kotlin.test.assertIs

class RBTest {
    private lateinit var rbTree: RBTree<Int, Int>

    @BeforeEach
    fun setup() {
        rbTree = RBTree<Int, Int>()
    }

    @Test
    fun `simple array test`() {
        val array = intArrayOf(0, 1, 2, 3, 4)
        for (i in array) {
            rbTree[i] = i
        }
        val expectedGet: Array<Int?> = Array(5, { i -> i })
        val actualGet: Array<Int?> = Array(5, { i -> rbTree.get(i) })
        assertContentEquals(expectedGet, actualGet, "Get method must return the value associated with the key")
        val expectedSize = 5
        val actualSize = rbTree.size
        assertEquals(expectedSize, actualSize, "Size the tree must correspond to the number key-value pairs")
    }

    @Test
    fun `size of red black tree with duplicate keys`() {
        for (i in 1..10) {
            rbTree[i] = i + 30
        }
        for (i in 6..10) {
            rbTree[i] = 0
        }

        val expectedSize = 10
        val actualSize = rbTree.size
        assertEquals(
            expectedSize,
            actualSize,
            "Size of the tree must not change after overwriting an existing key",
        )
    }

    @Test
    fun `simple array set test`() {
        for (i in 0 .. 19) {
            rbTree.set(i, i)
        }

        for (i in 0 .. 19) {
            val expectedContains = rbTree.get(i)
            assertEquals(expectedContains, i, "Added and not removed element does not contain in the tree")
        }

        val expectedGet: Array<Int?> = Array(20, { i -> i })
        val actualGet: Array<Int?> = Array(20, { i -> rbTree.get(i) })
        assertContentEquals(expectedGet, actualGet, "Get method must return the value associated with the key")
        val expectedSize = 20
        val actualSize = rbTree.size
        assertEquals(expectedSize, actualSize, "Size the tree must correspond to the number key-value pairs")
    }
    @Test
    fun `simple array set remove test`() {
        for (i in 0 .. 19) {
            rbTree.set(i, i)
        }
        for (i in 10 .. 19) {
            rbTree.remove(i)
        }
        for (i in 10 .. 19) {
            val expectedNotContains = rbTree.get(i)
            assertEquals(expectedNotContains, null, "Removed element contains in the tree")
        }
        for (i in 0 .. 9) {
            val expectedContains = rbTree.get(i)
            assertEquals(expectedContains, i,  "Added and not removed element does not contain in the tree")
        }

        val expectedGet: Array<Int?> = Array(10, { i -> i })
        val actualGet: Array<Int?> = Array(10, { i -> rbTree.get(i) })
        assertContentEquals(expectedGet, actualGet, "Get method must return the value associated with the key")
        val expectedSize = 10
        val actualSize = rbTree.size
        assertEquals(expectedSize, actualSize, "Size the tree must correspond to the number key-value pairs")
    }

    @Test
    fun `adding and removing all elements test`() {
        for (i in 0 .. 19) {
            rbTree.set(i, i)
        }
        for (i in 0 .. 19) {
            rbTree.remove(i)
        }
        for (i in 0 .. 19) {
            val expectedNotContains = rbTree.get(i)
            assertEquals(expectedNotContains, null, "Removed element contains in the tree")
        }

        val expectedGet: Array<Int?> = emptyArray()
        val actualGet: Array<Int?> = Array(rbTree.size, { i -> rbTree.get(i) })
        assertContentEquals(expectedGet, actualGet, "Get method must return the value associated with the key")
        val expectedSize = 0
        val actualSize = rbTree.size
        assertEquals(expectedSize, actualSize, "Size the tree must correspond to the number key-value pairs")
    }
}