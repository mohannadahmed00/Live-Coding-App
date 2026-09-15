package scratch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.milliseconds

/**
 * CLEAN START for an algorithm, collection, OOP, or basic Kotlin question.
 * Click the green Run icon beside main(). Keep the first version small and working.
 * Useful quick checks: empty input, one item, duplicates, invalid input, and a normal case.
 */
fun main() = runBlocking{
    // Write the interview solution here.
    val jobs = listOf(
        launch { println("hello world 1") },
        launch {
            delay(3000L)
            println("hello world 2") },
        launch { println("hello world 3") },
    )
    jobs.joinAll()
}

/**
 * CLEAN START for a coroutine-only question that does not need Android classes.
 * Call this from main using runBlocking { coroutineExercise() } while practicing locally.
 * In production Android code, use a lifecycle-owned scope instead of runBlocking.
 */
suspend fun coroutineExercise() {
    // Write the coroutine solution here.
}
