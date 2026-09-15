package com.giraffe.livecodigapp.playground

/**
 * QUESTION: Why prefer a data class for a value model?
 * ANSWER: Kotlin generates value-based equals/hashCode, toString, componentN, and copy.
 * Use a regular class when identity or custom lifecycle/behavior is more important than value equality.
 */
data class InterviewUser(val id: Long, val name: String)

/**
 * QUESTION: Why use a sealed hierarchy for results or UI state?
 * ANSWER: The compiler knows every direct subtype, so a when expression can be exhaustive without
 * an else branch. Each state can also carry only the data it needs.
 */
sealed interface LoadResult<out T> {
    data object Loading : LoadResult<Nothing>
    data class Success<T>(val value: T) : LoadResult<T>
    data class Error(val message: String) : LoadResult<Nothing>
}

fun resultMessage(result: LoadResult<List<InterviewUser>>): String = when (result) {
    LoadResult.Loading -> "Loading"
    is LoadResult.Success -> "Loaded ${result.value.size} users"
    is LoadResult.Error -> result.message
}

/**
 * QUESTION: map vs flatMap?
 * ANSWER: map transforms each item into one item. flatMap transforms each item into a collection
 * and flattens those collections into one list.
 */
fun uniqueWordsBySentence(sentences: List<String>): Set<String> =
    sentences
        .flatMap { sentence -> sentence.split(" ") }
        .map { word -> word.trim().lowercase() }
        .filter { word -> word.isNotBlank() }
        .toSet()

/**
 * QUESTION: let, run, apply, and also—how do you remember them?
 * ANSWER:
 * - let/run return the lambda result; apply/also return the receiver.
 * - let/also name the receiver `it`; run/apply use `this`.
 * Typical uses: let for nullable transforms, apply for configuration, also for side effects/logging,
 * and run for computing a result with several receiver operations.
 */
fun createDisplayUser(id: Long, rawName: String?): InterviewUser =
    InterviewUser(id = id, name = rawName?.trim()?.takeIf(String::isNotEmpty) ?: "Unknown")
