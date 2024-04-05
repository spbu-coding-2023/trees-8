package trees.bstest

import trees.implementations.SimpleTree
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class BSTreeTest {
    private lateinit var simpleTree: SimpleTestTree<Int, Int>

    @BeforeEach
    fun setup() {
        simpleTree = SimpleTestTree()
    }

    @Test
    fun testCheckEmptyTree() {
        assertNull(simpleTree.getRootSimple())
        assertEquals(0, simpleTree.size)
    }

    @Test
    fun testInsertAndFind() {
        val array = intArrayOf(0, 1, 2, 3, 4, 5, 6, 7)
        for (i in array) {
            simpleTree[i] = -i
        }
        val expectedGet: Array<Int?> = Array(7) { i -> -i }
        val actualGet: Array<Int?> = Array(7) { i -> simpleTree[i] }
        assertContentEquals(expectedGet, actualGet, "Get method must return the value associated with the key")
        val expectedSize = array.size
        val actualSize = simpleTree.size
        assertEquals(expectedSize, actualSize)
    }

    @Test
    fun testSimpleDelete() {
        val array = intArrayOf(0, 1, 2, 3, 4, 5, 6)
        for (i in array) {
            simpleTree[i] = i
        }

        val expectedResult = 3
        val actualResult = simpleTree.remove(3)
        assertEquals(expectedResult, actualResult)

        val expectedSize = array.size - 1
        val actualSize = simpleTree.size
        assertEquals(expectedSize, actualSize)
    }

    @Test
    fun testSizeWithDuplicateKeys() {
        for (i in 1..10) {
            simpleTree[i] = i + 30
        }
        for (i in 6..10) {
            simpleTree[i] = 0
        }

        val expectedSize = 10
        val actualSize = simpleTree.size
        assertEquals(expectedSize, actualSize)
    }

    @Test
    fun testTwiceKeys() {
        for (i in 1..10) {
            simpleTree[i] = 30 + i
        }
        for (i in 1..10) {
            simpleTree[i] = 0
        }

        val expectedSize = 10
        val actualSize = simpleTree.size
        assertEquals(expectedSize, actualSize)
    }

    @Test
    fun testRemoveNonExistentKey() {
        simpleTree[0] = 15
        val actualResult = simpleTree.remove(3)
        val expectedResult = null
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun testRemoveRootOfLeftSubtree() {
        val keys = intArrayOf(0, -1, 1, -2)
        for (i in keys) {
            simpleTree[i] = i
        }
        simpleTree.remove(-1)

        val expectedResult: Array<Int?> = arrayOf(0, null, 1, -2)
        val actualResult = Array(keys.size) { j -> simpleTree[keys[j]] }
        assertContentEquals(expectedResult, actualResult)
    }

    @Test
    fun testRemoveRootOfRightSubtree() {
        val keys = intArrayOf(0, -1, 2, 1)
        for (i in keys) {
            simpleTree[i] = i
        }
        simpleTree.remove(-1)

        val expectedResult: Array<Int?> = arrayOf(0, null, 2, 1)
        val actualResult = Array(keys.size) { j -> simpleTree[keys[j]] }
        assertContentEquals(expectedResult, actualResult)
    }

    @Test
    fun testRemoveRootWith2Children() {
        val keys = intArrayOf(0, -1, 2, 1, 3)
        for (key in keys) {
            simpleTree[key] = key
        }
        simpleTree.remove(2)

        val expectedResult: Array<Int?> = arrayOf(0, -1, null, 1, 3)
        val actualResult = Array(keys.size) { i -> simpleTree[keys[i]] }
        assertContentEquals(expectedResult, actualResult)
    }

    @Test
    fun testRemoveRightRootWithoutChildren() {
        val keys = intArrayOf(0, -1, 2)
        for (key in keys) {
            simpleTree[key] = key
        }
        simpleTree.remove(2)

        val expectedResult: Array<Int?> = arrayOf(0, -1, null)
        val actualResult = Array(keys.size) { i -> simpleTree[keys[i]] }
        assertContentEquals(expectedResult, actualResult)
    }
}



















