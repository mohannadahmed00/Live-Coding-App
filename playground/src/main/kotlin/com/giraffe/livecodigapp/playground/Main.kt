package com.giraffe.livecodigapp.playground

import kotlinx.coroutines.runBlocking

/**
 * START HERE for a simple Kotlin live-coding question.
 *
 * In Android Studio, open this file and click the green Run icon beside main().
 * During the interview:
 * 1. Repeat the problem and clarify inputs/edge cases.
 * 2. Say the simple approach first, then improve it.
 * 3. Write a small working solution before refactoring.
 * 4. Test empty, one-item, normal, and invalid/duplicate cases.
 * 5. State time and space complexity.
 */
fun main() = runBlocking {
    println("Two Sum: ${twoSum(listOf(2, 7, 11, 15), target = 9)}")
    println("Valid brackets: ${hasValidBrackets("{[()]}")}")
    println(
        "Merged intervals: ${mergeIntervals(
            listOf(Interval(1, 3), Interval(2, 6), Interval(8, 10), Interval(9, 12))
        )}"
    )
    println("Dashboard: ${loadDashboard(FakeDashboardApi())}")
}
