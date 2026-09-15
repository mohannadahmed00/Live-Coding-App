package com.giraffe.livecodigapp.playground

/**
 * QUESTION: Given numbers and a target, return the indices of two numbers that add to the target.
 * Example: [2, 7, 11, 15], target 9 -> (0, 1).
 *
 * ANSWER: Store each visited number and its index in a HashMap. Before storing the current
 * number, look for target - number. This avoids the O(n^2) nested-loop solution.
 *
 * Complexity: O(n) time and O(n) extra space.
 * Clarify in an interview: Is exactly one answer guaranteed? Can the same element be used twice?
 */
fun twoSum(numbers: List<Int>, target: Int): Pair<Int, Int>? {
    val indexByValue = mutableMapOf<Int, Int>()

    numbers.forEachIndexed { index, number ->
        val needed = target - number
        val previousIndex = indexByValue[needed]
        if (previousIndex != null) return previousIndex to index

        indexByValue[number] = index
    }

    return null
}

/**
 * QUESTION: Determine whether (), [], and {} brackets are correctly balanced and nested.
 *
 * ANSWER: Push opening brackets onto a stack. A closing bracket must match the most recent
 * opening bracket. ArrayDeque is a good stack on Kotlin/JVM; Stack is an old synchronized type.
 *
 * Complexity: O(n) time and O(n) space in the worst case.
 */
fun hasValidBrackets(text: String): Boolean {
    val openingForClosing = mapOf(')' to '(', ']' to '[', '}' to '{')
    val stack = ArrayDeque<Char>()

    for (character in text) {
        when {
            character in openingForClosing.values -> stack.addLast(character)
            character in openingForClosing -> {
                if (stack.removeLastOrNull() != openingForClosing[character]) return false
            }
        }
    }

    return stack.isEmpty()
}

data class Interval(val start: Int, val end: Int) {
    init {
        require(start <= end) { "Interval start must be <= end" }
    }
}

/**
 * QUESTION (from the mock interview PDF): Merge all overlapping intervals.
 * Example: [[1,3], [2,6], [8,10], [9,12]] -> [[1,6], [8,12]].
 *
 * ANSWER: Sort by start, then scan once. If the next interval starts before or exactly when
 * the current interval ends, extend the current interval. Otherwise, save it and start a new one.
 *
 * Complexity: O(n log n) time for sorting, followed by an O(n) scan; O(n) output space.
 * If input is already sorted, time becomes O(n). This solution treats touching intervals as
 * overlapping; ask whether [1,2] and [2,3] should merge before coding.
 */
fun mergeIntervals(intervals: List<Interval>): List<Interval> {
    if (intervals.size <= 1) return intervals

    val sorted = intervals.sortedBy(Interval::start)
    val merged = mutableListOf<Interval>()
    var current = sorted.first()

    for (next in sorted.drop(1)) {
        if (next.start <= current.end) {
            current = current.copy(end = maxOf(current.end, next.end))
        } else {
            merged += current
            current = next
        }
    }

    merged += current
    return merged
}

/**
 * QUESTION: Implement binary search in a sorted list.
 *
 * ANSWER: Compare with the middle element and discard half of the remaining range each time.
 * Complexity: O(log n) time and O(1) space for this iterative version.
 */
fun binarySearch(sortedNumbers: List<Int>, target: Int): Int {
    var low = 0
    var high = sortedNumbers.lastIndex

    while (low <= high) {
        // This form avoids the overflow possible in (low + high) / 2.
        val middle = low + (high - low) / 2
        when {
            sortedNumbers[middle] < target -> low = middle + 1
            sortedNumbers[middle] > target -> high = middle - 1
            else -> return middle
        }
    }

    return -1
}
