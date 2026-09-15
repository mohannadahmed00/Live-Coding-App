package com.giraffe.livecodigapp.playground

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class AlgorithmsTest {
    @Test
    fun `two sum returns matching indices`() {
        assertEquals(0 to 1, twoSum(listOf(2, 7, 11, 15), 9))
        assertEquals(0 to 1, twoSum(listOf(3, 3), 6))
        assertNull(twoSum(listOf(1, 2), 10))
    }

    @Test
    fun `brackets must be balanced and correctly nested`() {
        assertTrue(hasValidBrackets("{[()]}") )
        assertTrue(hasValidBrackets(""))
        assertFalse(hasValidBrackets("([)]"))
        assertFalse(hasValidBrackets("("))
    }

    @Test
    fun `merge intervals handles overlap nesting and gaps`() {
        val input = listOf(
            Interval(8, 10),
            Interval(2, 6),
            Interval(1, 3),
            Interval(9, 12),
            Interval(3, 4),
        )

        assertEquals(listOf(Interval(1, 6), Interval(8, 12)), mergeIntervals(input))
        assertEquals(emptyList<Interval>(), mergeIntervals(emptyList()))
        assertEquals(listOf(Interval(1, 10)), mergeIntervals(listOf(Interval(1, 10), Interval(2, 3))))
    }

    @Test
    fun `binary search returns index or minus one`() {
        assertEquals(3, binarySearch(listOf(1, 3, 5, 7, 9), 7))
        assertEquals(-1, binarySearch(listOf(1, 3, 5, 7, 9), 4))
    }
}
