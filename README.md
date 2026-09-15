# Android Live-Coding Starter

`main` is intentionally minimal and ready for a live-coding interview.

## Start here

| Area | File | Purpose |
|---|---|---|
| Kotlin / algorithms | `playground/.../Playground.kt` | Fast JVM `main()` with no emulator |
| ViewModel / Flow | `app/.../scratch/ScratchViewModel.kt` | Empty lifecycle-aware starting point |
| Jetpack Compose | `app/.../scratch/SimpleScreen.kt` | Empty screen wired to the Android app |

All three files compile before you add an interview solution.

## Run

```powershell
.\gradlew.bat :playground:run
.\gradlew.bat :app:assembleDebug
```

## Branches

- `main` - clean interview workspace.
- `examples` - solved algorithms, coroutines, Compose/ViewModel examples, tests, and the recap cheat sheet.

```powershell
git switch examples
git switch main
```

Keep interview work on a temporary branch if you want `main` to remain reusable.
