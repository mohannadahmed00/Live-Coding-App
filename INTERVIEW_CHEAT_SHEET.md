# Middle Android Interview Cheat Sheet

These are concise answers to the highest-probability questions in the supplied Android master bank and mock-interviewer pack. Practice saying each answer in your own words in under two minutes.

## Kotlin and coroutines

### What does `suspend` mean?

It marks a function that can pause and resume without blocking its thread. It does not make the call asynchronous and does not switch dispatchers. Use `Dispatchers.IO` only for genuinely blocking I/O; well-designed suspending APIs already avoid blocking.

### What is structured concurrency?

Coroutines belong to a scope and parent job. The parent waits for its children, and cancellation normally propagates through the hierarchy. Screen work belongs in `viewModelScope` or `lifecycleScope`, not `GlobalScope`. Use `supervisorScope` when one child failure should not cancel its siblings.

### `launch` vs `async`?

`launch` returns a `Job` and is for work whose result is not returned. `async` returns `Deferred<T>`; call `await()` for its result. Do not use `async` as fire-and-forget, and do not immediately `await()` a single `async` when direct sequential code is clearer.

### Flow vs StateFlow vs SharedFlow?

- `Flow` is normally cold: upstream work starts separately for each collector.
- `StateFlow` is hot, always has a current value, and is a natural fit for immutable screen state.
- `SharedFlow` is a configurable hot broadcast stream with replay/buffering. It can model shared events, but one-off UI events need careful lifecycle semantics.

### How do you build search-as-you-type?

Own a private query `MutableStateFlow`, then use `debounce`, trim, `distinctUntilChanged`, and `flatMapLatest`. The latest operator cancels stale work. Map loading/success/error explicitly and rethrow `CancellationException` if a broad catch could receive it. See `SearchViewModel.kt`.

### How do you protect shared mutable state?

For a simple atomic value, use an atomic update. For a suspending critical section, use `Mutex.withLock`. For concurrent token refresh, lock and then check the token again inside the lock; another caller may have refreshed it while this caller waited. See `TokenRefresher`.

## Android architecture and Compose

### What survives in a ViewModel?

A ViewModel survives configuration changes while its owner remains in scope/back stack. It does not inherently survive process death. Use `SavedStateHandle` for small restorable state and persistent storage/repositories for durable data.

### Explain MVVM with unidirectional data flow.

The UI renders immutable state. User events travel upward to the ViewModel. The ViewModel coordinates domain/data work and publishes new state. Expose read-only `StateFlow`; keep mutable state private. The repository owns data-source policy, not the composable.

### When is a use-case layer useful?

Use it when business logic is reused, combines repositories, has meaningful rules, or needs an independent test boundary. A pass-through class with no policy is ceremony. Add the layer because responsibilities demand it, not merely to match a diagram.

### How should REST errors reach the UI?

Convert transport/parsing failures into controlled data/domain errors before the UI. Distinguish authentication, server, connectivity, parsing, and domain failures. A ViewModel maps them to UI state. Do not leak Retrofit or raw `IOException` details into composables.

### What causes recomposition?

Reading observable Compose state makes that composable eligible to re-run when the value changes. Recomposition is function re-execution, not necessarily a redraw of the whole screen. Compose can skip unaffected work. Investigate unstable inputs and expensive work with measurement before optimizing.

### `remember`, `rememberSaveable`, or ViewModel?

`remember` survives recomposition while the composable remains in the composition. `rememberSaveable` also restores saveable UI state across Activity recreation and eligible saved-state restoration. ViewModel owns screen/business state and data work. Durable data still belongs in persistence.

### What is state hoisting?

Move state to a caller and give the child `value` plus event callbacks. That creates one source of truth and makes the content composable reusable, previewable, and easier to test. See `SearchRoute` and `SearchScreen`.

### How do you collect StateFlow in Compose?

Use `collectAsStateWithLifecycle()` on Android, commonly in a route-level composable. Pass plain values and callbacks into stateless content. Lifecycle-aware collection stops unnecessary upstream collection while the screen is inactive.

### Compose side effects?

Use `LaunchedEffect(key)` for coroutine effects tied to composition and restarted when the key changes. Use `DisposableEffect` for setup/cleanup pairs such as listeners. Use `rememberCoroutineScope` for event-handler coroutines. Never launch network work directly in the composable body because recomposition can duplicate it.

## Data, testing, and design

### Offline-first single source of truth?

The UI observes Room/database state. Refresh fetches network data and writes it into the database; the database emission updates the UI. Show cached data immediately. If refresh fails, usually retain cached content and expose a non-blocking refresh error.

### How do you test ViewModel + Flow?

Inject a fake repository and test dispatcher, replace `Dispatchers.Main`, use `runTest`, start collection for lazily shared flows, drive events, advance virtual time, and assert states. Avoid real delays. `SearchViewModelTest` demonstrates stale-result cancellation.

### Useful collection choices and complexity

- `List`: ordered/indexed sequence; search is O(n), `ArrayList` indexed access is O(1).
- `Set`: uniqueness and average O(1) hash membership.
- `Map`: key/value lookup, average O(1) with hashing.
- `ArrayDeque`: efficient stack/queue operations at both ends.
- Heap/priority queue: repeated min/max access; insert/remove O(log n).
- Sorting is commonly O(n log n).

### Merge overlapping intervals?

First clarify whether touching intervals count as overlap. Sort by start, scan once, extend the current interval on overlap, otherwise emit it. This is O(n log n) time and O(n) output space; already-sorted input is O(n). See `mergeIntervals` and its tests.

### Dagger building blocks?

`@Inject` constructor tells Dagger how to construct a type. `@Binds` maps an interface to an implementation. `@Provides` creates a dependency needing custom/configured construction, such as a Retrofit API. `@Module` groups bindings/providers, and `@Component` connects the graph and exposes entry points. Scope only when lifetime and identity requirements justify it.

## Last-minute reminders

- Ask clarifying questions before coding.
- Avoid `GlobalScope`, `!!`, mutable public state, and work launched in a composable body.
- Do not catch `Throwable` and accidentally swallow cancellation.
- Say what happens on configuration change, process death, cancellation, retry, and concurrent calls.
- Prefer a minimal correct solution, tests and edge cases, then refactoring.
