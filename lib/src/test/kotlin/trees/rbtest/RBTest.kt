package trees.rbtest

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import trees.implementations.RBTree
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class RBTest {
    private lateinit var rbTree: RBTree<Int, Int>

    @BeforeEach
    fun setup() {
        rbTree = RBTree()
    }

    @Test
    fun `simple array test`() {
        val array = intArrayOf(0, 1, 2, 3, 4)
        for (i in array) {
            rbTree[i] = i
        }
        val expectedGet: Array<Int?> = Array(5) { i -> i }
        val actualGet: Array<Int?> = Array(5) { i -> rbTree[i] }
        assertContentEquals(expectedGet, actualGet, "Get method must return the value associated with the key")
        val expectedSize = 5
        val actualSize = rbTree.size
        assertEquals(expectedSize, actualSize, "Size the tree must correspond to the number key-value pairs")
    }

    @Test
    fun `array test`() {
        val array = intArrayOf(2, 1, 3, 0, 4)
        for (i in array) {
            rbTree[i] = i
        }
        val expectedGet: Array<Int?> = Array(5) { i -> i }
        val actualGet: Array<Int?> = Array(5) { i -> rbTree[i] }
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
        for (i in 0..19) {
            rbTree[i] = i
        }

        for (i in 0..19) {
            val expectedContains = rbTree[i]
            assertEquals(expectedContains, i, "Added and not removed element does not contain in the tree")
        }

        val expectedGet: Array<Int?> = Array(20) { i -> i }
        val actualGet: Array<Int?> = Array(20) { i -> rbTree[i] }
        assertContentEquals(expectedGet, actualGet, "Get method must return the value associated with the key")
        val expectedSize = 20
        val actualSize = rbTree.size
        assertEquals(expectedSize, actualSize, "Size the tree must correspond to the number key-value pairs")
    }

    @Test
    fun `simple array set remove test`() {
        for (i in 0..19) {
            rbTree[i] = i
        }
        for (i in 10..19) {
            rbTree.remove(i)
        }
        for (i in 10..19) {
            val expectedNotContains = rbTree[i]
            assertEquals(expectedNotContains, null, "Removed element contains in the tree")
        }
        for (i in 0..9) {
            val expectedContains = rbTree[i]
            assertEquals(expectedContains, i, "Added and not removed element does not contain in the tree")
        }

        val expectedGet: Array<Int?> = Array(10) { i -> i }
        val actualGet: Array<Int?> = Array(10) { i -> rbTree[i] }
        assertContentEquals(expectedGet, actualGet, "Get method must return the value associated with the key")
        val expectedSize = 10
        val actualSize = rbTree.size
        assertEquals(expectedSize, actualSize, "Size the tree must correspond to the number key-value pairs")
    }

    @Test
    fun `not simple array set remove test`() {

        for (i in 19 downTo 0) {
            rbTree[19 - i] = i
        }
        for (i in 8..19) {
            rbTree.remove(i)
        }

        for (i in 8..19) {
            val expectedNotContains = rbTree[i]
            assertEquals(expectedNotContains, null, "Removed element contains in the tree")
        }
        for (i in 0..7) {
            val expectedContains = rbTree[i]
            assertEquals(expectedContains, 19 - i, "Added and not removed element does not contain in the tree")
        }
    }

    @Test
    fun `adding and removing all elements test`() {
        for (i in 0..19) {
            rbTree[i] = i
        }
        for (i in 0..19) {
            rbTree.remove(i)
        }
        for (i in 0..19) {
            val expectedNotContains = rbTree[i]
            assertEquals(expectedNotContains, null, "Removed element contains in the tree")
        }

        val expectedGet: Array<Int?> = emptyArray()
        val actualGet: Array<Int?> = Array(rbTree.size) { i -> rbTree[i] }
        assertContentEquals(expectedGet, actualGet, "Get method must return the value associated with the key")
        val expectedSize = 0
        val actualSize = rbTree.size
        assertEquals(expectedSize, actualSize, "Size the tree must correspond to the number key-value pairs")
    }

    @Test
    fun `rotate left`() {
        val keys = intArrayOf(0, -1, 4, 2, 6, -2, 1, 3, 5, 7)
        for (key in keys) rbTree[key] = key
        rbTree.remove(-2)

        val expectedResult = arrayOf(0, -1, 4, 2, 6, null, 1, 3, 5, 7)
        val actualResult = Array(keys.size) { i -> rbTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
        )
    }

    @Test
    fun `rotate right`() {
        val keys = intArrayOf(0, 1, -1, -3, -2, -4)
        for (key in keys) rbTree[key] = key

        val expectedResult = Array<Int?>(keys.size) { i -> keys[i] }
        val actualResult = Array(keys.size) { i -> rbTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
        )
    }

    @Test
    fun `remove root with rotate`() {
        val keys = intArrayOf(4, 1, 6, 0, 3, 5, 2)
        for (key in keys) rbTree[key] = key
        rbTree.remove(4)
        Assertions.assertTrue(rbTree.root != null)
        val expectedResult = arrayOf(null, 1, 6, 0, 3, 5, 2)
        val actualResult = Array(keys.size) { i -> rbTree[keys[i]] }
        assertContentEquals(
            expectedResult,
            actualResult,
        )
    }
    
    @Test
    fun `remove a non-existent key`() {
        rbTree[0] = 15
        rbTree.remove(0)

        val expectedResult = null
        val actualResult = rbTree.remove(0)
        assertEquals(
            expectedResult,
            actualResult,
            "Remove a non-existent key must return null",
        )
    }
}
