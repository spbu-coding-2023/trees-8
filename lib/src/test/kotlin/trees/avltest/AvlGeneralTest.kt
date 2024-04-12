package trees.avltest

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import trees.implementations.AvlTree
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import trees.avltest.AvlSpecificTest.Companion.checkTreeInvariant
import kotlin.test.assertIs

class AvlGeneralTest {
    private lateinit var avlTree: AvlTree<Int,Int>

    @BeforeEach
    fun setup() {
        avlTree = AvlTree()
    }

    @Test
    fun `simple array test`() {
        val array = intArrayOf(0, 1, 2, 3, 4)
        for (i in array) {
            avlTree[i] = -i
        }
        val expectedGet: Array<Int?> = Array(5, { i -> -i })
        val actualGet: Array<Int?> = Array(5, { i -> avlTree[i] })
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
            expectedSize,
            actualSize,
            "Size of the tree must not change after overwriting an existing key",
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
            expectedSize,
            actualSize,
            "Size of the tree must not change after overwriting an existing key",
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `set return`() {
        assertEquals(
            avlTree.set(44, -37), null,
            "Set must return null if set key was not present in the tree"
        )

        assertEquals(
            avlTree.set(44, 152), -37,
            "Set must return previous value associated with the key"
        )
    }

    @Test
    fun `simple remove`() {
        avlTree[0] = 5

        val expectedResult = 5
        val actualResult = avlTree.remove(0)
        val expectedSize = 0
        val actualSize = avlTree.size

        assertEquals(
            expectedResult,
            actualResult,
            "Remove must return removed value",
        )
        assertEquals(
            expectedSize,
            actualSize,
            "Size of the tree must decrease after removing the existing key",
        )
    }

    @Test
    fun `remove a non-existent key`() {
        avlTree[0] = 15
        avlTree.remove(0)

        val expectedResult = null
        val actualResult = avlTree.remove(0)
        assertEquals(
            expectedResult,
            actualResult,
            "Remove a non-existent key must return null",
        )
    }

    @Test
    fun `remove root of left subtree with existing left vertex`() {
        val keys = intArrayOf(0, -1, 1, -2)
        for (key in keys) avlTree[key] = key
        avlTree.remove(-1)

        val expectedResult = arrayOf(0, null, 1, -2)
        val actualResult = Array(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult,
            actualResult,
            "Tree must save all other vertices after remove vertex with existing left vertex",
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `remove root of right subtree with existing left vertex`() {
        val keys = intArrayOf(0, -1, 2, 1)
        for (key in keys) avlTree[key] = key
        avlTree.remove(2)

        val expectedResult = arrayOf(0, -1, null, 1)
        val actualResult = Array(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult,
            actualResult,
            "Tree must save all other vertices after remove root of right subtree with existing left vertex",
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `remove with right subtree of height 1`() {
        val keys = intArrayOf(0, -1, 3, 1, 2)
        for (key in keys) avlTree[key] = key
        avlTree.remove(3)

        val expectedResult = arrayOf(0, -1, null, 1, 2)
        val actualResult = Array(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult,
            actualResult,
            "Tree must save all other vertices after remove vertex with right subtree of height 1",
        )
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `remove with right subtree of height more than 1`() {
        val keys = intArrayOf(0, -1, 3, -2, 1, 6, 4, 7)
        for (key in keys) avlTree[key] = key
        avlTree.remove(3)

        val expectedResult = arrayOf(0, -1, null, -2, 1, 6, 4, 7)
        val actualResult = Array(keys.size, { i -> avlTree[keys[i]] })
        assertContentEquals(
            expectedResult,
            actualResult,
            "Tree must save all other vertices after remove vertex with right subtree of height >1",
        )
        checkTreeInvariant(avlTree.root)
        avlTree[5] = 5
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `simple iteration`() {
        val keys = intArrayOf(1, 7, 4, 9, 2, -44, 3)
        val values = intArrayOf(19, 14, 31, 17, 12, -34, 0)
        for (i in keys.indices) {
            avlTree[keys[i]] = values[i]
        }

        val expectedResult: Array<Int?> = arrayOf(-34, 19, 12, 0, 31, 14, 17)
        val iterator = avlTree.iterator()
        val actualResult: Array<Int?> = Array(7, { iterator.next().second })
        assertContentEquals(expectedResult, actualResult, "Iterator must return vertices in keys order")
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `iteration outOfBonds exception`() {
        val keys = intArrayOf(1, 3, 5, 7, 9, 12, 342, -488)
        for (key in keys) avlTree[key] = key
        val iterator = avlTree.iterator()
        for (key in keys) {
            assertIs<Pair<Int, Int>>(
                iterator.next(), "iterator.next() must return Pair<K,V> while all keys is not bypassed"
            )
        }
        assertThrows<IndexOutOfBoundsException>(
            "Iterator must throw IndexOutOfBondsException after next() if all keys is bypassed"
        ) {
            iterator.next()
        }
    }

    @Test
    fun clear() {
        val keys = intArrayOf(1, 2, 7, 2, 4, 9)
        for (key in keys) avlTree[key] = -key
        avlTree.clear()

        assertEquals(avlTree.size, 0, "Size of the tree must be 0 after clear")
        assertEquals(avlTree.root, null, "Root of the tree must be null after clear")
    }

    @Test
    fun `contains key`() {
        val keys = intArrayOf(1, 7, 3, -34, 45)
        for (key in keys) avlTree[key] = key

        for (key in keys) {
            assertEquals(
                avlTree.containsKey(key), true,
                "containsKey must return true for keys existing in the tree"
            )
        }
        avlTree.remove(-34)
        val keysNotInTheTree = intArrayOf(102, 57, -22, -34)
        for (key in keysNotInTheTree) {
            assertEquals(
                avlTree.containsKey(key), false,
                "containsKey must return false for keys not existing in the tree"
            )
        }
    }

    @Test
    fun isNotEmpty() {
        assertEquals(avlTree.isNotEmpty(), false, "isNotEmpty must return false if tree is empty")

        val keys = intArrayOf(1, 45, -10045, 0, -10000, 3000004)
        for (key in keys) avlTree[key] = key

        assertEquals(avlTree.isNotEmpty(), true, "isNotEmpty must return true if tree is not empty")

        avlTree.clear()

        assertEquals(avlTree.isNotEmpty(), false, "isNotEmpty must return false if tree is empty")

    }

    @Test
    fun isEmpty() {
        assertEquals(avlTree.isEmpty(), true, "isEmpty must return true if tree is empty")

        avlTree[14] = 902

        assertEquals(avlTree.isEmpty(), false, "isEmpty must return false if tree is not empty")

        avlTree.remove(14)

        assertEquals(avlTree.isEmpty(), true, "isEmpty must return true if tree is empty")
    }

    @Test
    fun max() {
        val keys = intArrayOf(0, -1, 1, -45, 5, 4, 2, 3)
        for (key in keys) avlTree[key] = key

        val expectedResult = Pair(5, 5)
        val actualResult = avlTree.max()

        assertEquals(expectedResult, actualResult, "Max must return Pair<K,V>s by max key in the tree")
        checkTreeInvariant(avlTree.root)

    }

    @Test
    fun `max after remove max key with double rotate`() {
        val keys = intArrayOf(5, 1, 6, 0, 3, 7, 2, 4)
        for (key in keys) avlTree[key] = key
        avlTree.remove(7)

        val expectedResult = Pair(6, 6)
        val actualResult = avlTree.max()

        assertEquals(expectedResult, actualResult, "Max must return Pair<K,V> by max key in the tree")
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun min() {
        val keys = intArrayOf(54, 88, 4332, -46, -2)
        for (key in keys) avlTree[key] = key


        val expectedResult = Pair(-46, -46)
        val actualResult = avlTree.min()
        assertEquals(expectedResult, actualResult, "Min must return Pair<K,V> by min key in the tree")
        checkTreeInvariant(avlTree.root)


    }

    @Test
    fun `min after remove min key with double rotate`() {
        val keys = intArrayOf(2, 1, 6, 0, 4, 7, 3, 5)
        for (key in keys) avlTree[key] = key
        avlTree.remove(0)

        val expectedResult = Pair(1, 1)
        val actualResult = avlTree.min()

        assertEquals(expectedResult, actualResult, "Min must return Pair<K,V> by min key in the tree")
        checkTreeInvariant(avlTree.root)
    }

    @Test
    fun `getOrSet with existing key`() {
        avlTree[25] = -48

        val expectedResult = -48
        val actualResult = avlTree.getOrSet(25, 90)
        assertEquals(
            expectedResult, actualResult,
            "getOrSet must return value corresponding to the specified key if it's presented in the tree"
        )
    }

    @Test
    fun `getOrSet with non existing key`() {
        assertEquals(
            avlTree.getOrSet(0, -10500), null,
            "getOrSet must return null if key is not presented in the tree"
        )
        assertEquals(
            avlTree[0], -10500,
            "getOrSet must set specified key-value if key was not presented in the tree"
        )
    }

    @Test
    fun `getOrDefault with existing key`() {
        avlTree[45] = 45

        val expectedResult = 45
        val actualResult = avlTree.getOrDefault(45, "Default")
        assertEquals(
            expectedResult, actualResult,
            "getOrDefault must return value corresponding to the specified key if it's presented in the tree"
        )
    }

    @Test
    fun `getOrDefault with non existing key`() {
        val expectedResult = "Default"
        val actualResult = avlTree.getOrDefault(100, "Default")
        assertEquals(
            expectedResult, actualResult,
            "getOrDefault must return defaultValue if key is not presented in the tree"
        )
    }

    @Test
    fun `setIfAbsent when not absent`() {
        avlTree[520] = 520
        avlTree.setIfAbsent(520, -200)

        val expectedResult = 520
        val actualResult = avlTree[520]
        assertEquals(
            expectedResult, actualResult,
            "setIfAbsent must not change value if key is presented in the tree"
        )
    }

    @Test
    fun `setIfAbsent when absent`() {
        avlTree.setIfAbsent(-100, 250)

        val expectedResult = 250
        val actualResult = avlTree[-100]
        assertEquals(
            expectedResult, actualResult,
            "setIfAbsent must set specified value under specified key if key was not presented in the tree"
        )
    }
}