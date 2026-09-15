# Android Live-Coding Interview Playground

This project is prepared for a middle Android interview using the two supplied PDFs as the topic guide. It has two independent places to work:

| If they ask for… | Open this file | How to run it |
|---|---|---|
| A simple Kotlin/algorithm `main()` | `playground/src/main/kotlin/scratch/Scratch.kt` | Click the green icon beside `main()` |
| A solved algorithm reference | `playground/.../playground/Algorithms.kt` | Run `playground/.../playground/Main.kt` |
| A coroutine-only question | `playground/.../scratch/Scratch.kt` | Use `runBlocking` only in the local `main()` harness |
| A blank ViewModel | `app/.../scratch/ScratchViewModel.kt` | Add state/events, then run app tests |
| Debounced Search ViewModel + Flow | `app/.../interview/SearchViewModel.kt` | Run the app or its unit test |
| Compose state/UDF question | `app/.../interview/SearchScreen.kt` | Run the app; a Preview is included |
| Concurrent token refresh | `playground/.../playground/CoroutineQuestions.kt` | Run its unit test |

Use Android Studio’s **Navigate > File** and type the short filename; you do not need to browse the long paths.

## Before the interview

1. Open the project once and let Gradle sync finish.
2. Run the Android app on an emulator/device. Search for `flow` or `viewmodel`; type `error` to see error/retry state.
3. Run `Main.kt` once using its green gutter icon.
4. Run all local tests from the IDE, or in PowerShell:

   ```powershell
   .\gradlew.bat :playground:test :app:testDebugUnitTest
   ```

5. Open `Scratch.kt`, `ScratchViewModel.kt`, and `INTERVIEW_CHEAT_SHEET.md` in tabs before screen sharing.

No API key or network call is required. The Android demo uses a delayed local fake repository so debounce, loading, cancellation, empty results, and errors are visible.

## A strong live-coding rhythm

Say these points aloud as you work:

1. “Let me restate the problem and confirm the input, output, and edge cases.”
2. Explain the straightforward approach before optimizing.
3. Choose a data structure and explain why: Map for lookup, Set for uniqueness, ArrayDeque for stack/queue.
4. Write the smallest working solution.
5. Test normal input plus empty, single-item, duplicate/nested, and invalid cases as relevant.
6. State time and space complexity.
7. Only then refactor or discuss production hardening.

For Android questions, explicitly mention state ownership, lifecycle, cancellation, error mapping, testability, and whether work survives configuration change or process death.

## Project map

```text
app/                     Android + Compose module
  interview/             Solved Search ViewModel and stateless UDF screen
  scratch/               Clean ViewModel starting point
  test/                   Coroutine/ViewModel test example
playground/              Fast Kotlin/JVM module; no emulator needed
  playground/            Solved Kotlin, algorithm, coroutine examples
  scratch/               Clean main() and suspend-function starting point
  test/                   Algorithm and concurrent-token-refresh tests
KOTLIN_OOP_SOLID_100_QA.md  100 answered Kotlin, OOP, SOLID, and core-code questions
INTERVIEW_CHEAT_SHEET.md  Short verbal answers for the highest-probability topics
```

The namespace remains `livecodigapp` because renaming an Android package immediately before an interview adds risk; the visible project name has been corrected to “Live Coding App.”
