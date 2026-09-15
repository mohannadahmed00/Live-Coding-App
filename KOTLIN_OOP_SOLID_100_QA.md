# Kotlin, OOP, and SOLID: 100 Interview Questions with Answers

This bank expands the Kotlin and core-CS sections of the supplied Android Interview Master Bank. The wording and examples here are original and optimized for spoken interview answers.

Use it in three passes:

1. Read only each question and give a 30-60 second answer aloud.
2. Compare your answer with the short answer and example.
3. Re-code the examples without looking.

## Kotlin fundamentals

### 1. What is the difference between val, var, and const val?

**Answer:** val is a read-only reference: it can be assigned once. var is a reassignable reference. const val is a compile-time constant and is implicitly a val. A val can point to a mutable object, so it does not make the object immutable.

~~~kotlin
val users = mutableListOf("Ali")
users += "Mona"              // Allowed: the list mutates.
// users = mutableListOf()   // Not allowed: the reference cannot change.

var retryCount = 0
retryCount++

const val MAX_RETRIES = 3
~~~

### 2. Where can const val be declared, and what values are allowed?

**Answer:** It can be top-level or inside an object or companion object. Its value must be known at compile time and must be a String or primitive type. A runtime expression, nullable value, collection, or ordinary instance property cannot be const.

~~~kotlin
const val API_VERSION = 3

class Logger {
    companion object {
        const val TAG = "Logger"
    }
}
~~~

### 3. Is val the same as immutability?

**Answer:** No. val prevents reassignment of the reference, but the referenced object may still be mutable. Immutability means the observable state of the object cannot change. Prefer read-only interfaces and immutable models, but remember a read-only List may still wrap a mutable implementation.

### 4. What is type inference?

**Answer:** Kotlin can infer a variable or expression type from its initializer, such as val count = 3 becoming Int. Explicit types are still useful at public API boundaries, for empty collections, and when the intended abstraction is broader than the concrete value.

### 5. What are Any, Unit, and Nothing?

**Answer:** Any is the root of Kotlin's non-null type hierarchy. Unit represents successful completion with no meaningful result, similar to void but as a real value. Nothing has no possible value and marks code that never returns normally, such as throw or an infinite loop.

~~~kotlin
fun log(message: String): Unit = println(message)
fun fail(message: String): Nothing = throw IllegalStateException(message)
~~~

### 6. What does a question mark after a type mean?

**Answer:** It makes the type nullable. String cannot contain null, while String? can. A nullable receiver must be checked, safely called, converted with an Elvis expression, or deliberately asserted before non-null members are used.

### 7. What do the safe-call, Elvis, and non-null assertion operators do?

**Answer:** ?. evaluates a call only when the receiver is non-null. ?: supplies a fallback when the left side is null. !! asserts non-null and throws NullPointerException if the assertion is wrong. Prefer modeling and validation over !!.

~~~kotlin
val length = name?.length ?: 0
val user = repository.find(id) ?: return
~~~

### 8. What is the difference between as and as??

**Answer:** as performs a cast and throws ClassCastException when it fails. as? performs a safe cast and returns null on failure. Use as? when failure is an expected branch; use a type check or better abstraction when repeated casting suggests weak design.

### 9. What is the difference between == and ===?

**Answer:** == checks structural equality through a null-safe equals call. === checks referential identity: both references point to the exact same object. Business models usually need ==; === is useful only when identity itself matters.

### 10. What is a smart cast?

**Answer:** After Kotlin proves a value has a more specific type or is non-null, it lets you use that type without an explicit cast. Smart casts require a stable value; a mutable or externally overridable property may change between the check and use.

~~~kotlin
fun lengthOf(value: Any): Int =
    if (value is String) value.length else 0
~~~

### 11. Is when only a replacement for switch?

**Answer:** No. when is an expression that can return a value and match constants, types, ranges, and arbitrary conditions. With enum or sealed types it can be exhaustive, allowing the compiler to detect a missing case.

### 12. What is the difference between a statement and an expression?

**Answer:** An expression produces a value. In Kotlin, if, when, try, and blocks can be expressions. This often removes temporary mutable variables, but dense expression bodies should still be expanded when that improves readability.

### 13. What does val pair = 33 to 42 create?

**Answer:** It creates Pair(33, 42). The to function is an infix extension, not the range operator. Use 33..42 for an inclusive range and 33 until 42 for a range excluding 42.

### 14. What is the difference between .., until, and downTo?

**Answer:** .. normally creates an inclusive progression, until excludes the upper bound, and downTo creates a descending progression. step changes the increment. Be explicit about inclusive boundaries during algorithm questions.

### 15. What is destructuring?

**Answer:** Destructuring assigns componentN results to separate variables. Data classes and Pair provide component functions automatically. An underscore skips an unused component.

~~~kotlin
val (id, name) = User(7, "Mona")
map.forEach { (key, value) -> println(key + "=" + value) }
~~~

### 16. What is the difference between lateinit and lazy?

**Answer:** lateinit delays assignment of a non-null var and throws UninitializedPropertyAccessException if read too early. lazy computes a val on first access and caches it. lateinit supports later reassignment; lazy is normally initialized once.

### 17. What thread-safety modes does lazy support?

**Answer:** SYNCHRONIZED is the default and allows only one initializer across threads. PUBLICATION may run the initializer more than once but publishes one result. NONE adds no synchronization and is suitable only when access is confined to one thread.

### 18. What are backing fields and the field identifier?

**Answer:** Kotlin creates a backing field when a property needs stored state. Inside its accessor, field refers to that storage. Assigning the property name from its own setter recursively calls the setter.

~~~kotlin
var name: String = ""
    set(value) {
        field = value.trim()
    }
~~~

### 19. Can an interface property hold state?

**Answer:** An interface can declare a property contract or a computed accessor, but it has no instance backing field. The implementing class must provide storage when state is required.

### 20. Why prefer immutable state models?

**Answer:** Immutable values are easier to reason about, compare, share, and test. A state transition creates a new value rather than changing an object behind existing references. This is especially helpful for Compose, Flow, concurrency, and debugging.

## Classes, constructors, and objects

### 21. What is the difference between primary and secondary constructors?

**Answer:** The primary constructor is declared in the class header and participates in property initialization and init blocks. Secondary constructors are declared with constructor and must delegate to the primary constructor, directly or through another secondary constructor.

### 22. How many init blocks can a Kotlin class contain?

**Answer:** Any practical number; Kotlin defines no small fixed maximum. Property initializers and init blocks run in textual order whenever the primary initialization path executes. Prefer a small, understandable initialization sequence.

~~~kotlin
class Example(input: String) {
    val first = input.also { println("property 1") }

    init { println("init 1") }

    val second = input.length.also { println("property 2") }

    init { println("init 2") }
}
// Output order: property 1, init 1, property 2, init 2
~~~

### 23. What is the initialization order with a secondary constructor?

**Answer:** Delegation reaches the primary constructor first. Property initializers and init blocks then run in source order, and only afterward does the secondary-constructor body run. Therefore validation in init applies to every valid construction path.

### 24. Can constructors be abstract, open, or overridden?

**Answer:** No. Constructors initialize a particular class; they are not inherited members. An abstract class can have constructors, but they are concrete initialization paths invoked while constructing a subclass.

### 25. Why are Kotlin classes and members final by default?

**Answer:** Accidental inheritance makes contracts fragile. Kotlin requires open or abstract to explicitly allow extension and override to explicitly replace behavior. This makes intended variation points visible.

### 26. What is the difference between an abstract class and an interface?

**Answer:** An abstract class can own constructor state and shared instance implementation, and a class can inherit only one class. An interface expresses a capability or contract, may have default methods and computed properties, and can be implemented alongside many other interfaces.

### 27. What is the difference between a nested class and an inner class?

**Answer:** A nested class is static-like and has no implicit outer instance. A class marked inner retains an outer-instance reference and can access outer members.

~~~kotlin
class Screen(private val title: String) {
    class Args(val id: Long)          // Screen.Args(1)

    inner class ClickHandler {
        fun label(): String = title   // Has a Screen reference.
    }
}
~~~

### 28. What risk can an inner class introduce on Android?

**Answer:** Because it retains the outer object, a long-lived inner instance can keep an Activity, Fragment, or View alive and cause a memory leak. Prefer a nested class or top-level class unless access to the outer instance is genuinely required.

### 29. What is an object declaration?

**Answer:** It declares a named singleton, initialized lazily on first access in a thread-safe way. It cannot have an ordinary public constructor. It is useful for stateless shared utilities or a deliberate process-wide instance, but should not become a hidden service locator.

### 30. What is an object expression or anonymous object?

**Answer:** An object expression creates a new anonymous instance immediately. It can implement an interface, extend an open class, and contain state or methods. Unlike an object declaration, each evaluation can create a different instance.

~~~kotlin
val listener = object : View.OnClickListener {
    override fun onClick(view: View) {
        println("Clicked")
    }
}
~~~

### 31. When should you use an anonymous object instead of a lambda?

**Answer:** Use a lambda for a single-abstract-method interface when only one behavior is needed. Use an anonymous object when the type has several abstract members, needs extra local state, extends a class, or the object identity is important.

### 32. What is a companion object?

**Answer:** It is an object associated with a class and accessed through the class name. It can implement interfaces and contain factories or constants. Its members are not true Java static members unless interop annotations such as @JvmStatic or @JvmField are appropriate.

### 33. What does a data class generate?

**Answer:** From primary-constructor properties it generates equals, hashCode, toString, componentN, and copy. Properties declared only in the body do not participate in those generated operations.

### 34. Is data-class copy a deep copy?

**Answer:** No. copy is shallow. New constructor values can be supplied, but referenced mutable objects are shared unless they are copied separately. This is why deeply immutable state models are safer.

### 35. Can a data class be open or inherited from?

**Answer:** A data class is final and cannot be open. It may implement interfaces and can extend a compatible non-data base class, but another class cannot subclass the data class.

### 36. When should you use an enum versus a sealed hierarchy?

**Answer:** Use an enum for a fixed set of instances with the same general shape, such as sort direction. Use a sealed class or interface when cases need different data or behavior, such as Loading, Success(data), and Error(cause).

### 37. Can you create an anonymous direct subclass of a sealed class or interface?

**Answer:** No. Direct sealed subtypes must be named and satisfy sealed-hierarchy location rules. A permitted non-sealed subtype may allow further anonymous subclasses, but the direct sealed cases remain explicit.

### 38. What is a value class?

**Answer:** A value class wraps one value to create a stronger domain type, such as UserId instead of Long. The compiler can often avoid wrapper allocation, but boxing can still occur through generics, nullable values, or interface usage.

### 39. What is class delegation with by?

**Answer:** It forwards implementation of an interface to another object while allowing selected members to be overridden. It supports composition without writing repetitive forwarding methods.

~~~kotlin
class LoggingRepository(
    private val delegate: UserRepository,
) : UserRepository by delegate {
    override suspend fun refresh() {
        println("refresh")
        delegate.refresh()
    }
}
~~~

### 40. Why is calling open members from constructors dangerous?

**Answer:** Dynamic dispatch can call a subclass override before the subclass fields have been initialized. The override may observe default or invalid state. Constructors and init blocks should avoid overridable behavior.

## Visibility, functions, and language idioms

### 41. What is Kotlin's default visibility?

**Answer:** public. This applies to most classes, functions, properties, and constructors when no visibility modifier is written.

### 42. What does private mean at member and top-level scope?

**Answer:** A private member is visible only inside its declaring class or object. A private top-level declaration is visible throughout the same Kotlin file. Therefore "private means class-only" is incomplete for Kotlin.

### 43. What does protected mean in Kotlin?

**Answer:** A protected member is visible in its declaring class and subclasses. Unlike Java, it does not also grant package access, and protected is not valid for a top-level declaration.

### 44. What does internal mean?

**Answer:** It is visible within the same Kotlin module, where a module is usually a Gradle source compilation boundary. It is useful for hiding implementation from consumers, but JVM bytecode and Java interop do not enforce it as strongly as private.

### 45. Does Kotlin have Java's package-private visibility?

**Answer:** No direct equivalent. Kotlin offers public, internal, protected, and private. Top-level private gives file scope, while internal gives module scope.

### 46. What is the difference between overloading and overriding?

**Answer:** Overloading defines the same name with different parameter lists and is selected from the call signature. Overriding replaces an inherited open or abstract member and participates in runtime polymorphism.

### 47. What are default and named arguments?

**Answer:** A default argument lets a caller omit a parameter. A named argument states which parameter receives a value and can improve clarity or reorder arguments. They often avoid chains of overloads, but changing public parameter names can break named-call sites.

### 48. What is a higher-order function?

**Answer:** A function that receives a function value, returns one, or both. Kotlin collection operations such as map and filter are common examples.

### 49. What is the difference between a lambda and an anonymous function?

**Answer:** Both create function values. An anonymous function uses fun syntax and its unqualified return returns from that function. In an inline lambda, an unqualified return may return from the enclosing function unless labels or crossinline prevent it.

### 50. What is a function type with receiver?

**Answer:** A type such as HTML.() -> Unit lets the function body access an HTML receiver as this. It powers type-safe builders and DSLs. It should expose a focused vocabulary rather than hide complex control flow.

### 51. How are extension functions dispatched?

**Answer:** Statically, using the declared receiver type. They do not modify the class and are not virtual members. If a real member and an extension have the same applicable signature, the member wins.

### 52. Can an extension access private members of its receiver?

**Answer:** No. It has the same access as ordinary code at its declaration location. It can access public/internal members as allowed, but it is not actually inserted into the receiver class.

### 53. How do let, run, apply, also, and with differ?

**Answer:** let and also refer to the receiver as it; run, apply, and with use this. let, run, and with return the lambda result. apply and also return the original receiver. Choose by intent, and avoid deeply nested scope functions.

### 54. What does inline do?

**Answer:** It asks the compiler to substitute a function and eligible lambdas at call sites. This can remove function-object overhead, allow non-local lambda returns, and enable reified parameters. Excessive inlining increases bytecode.

### 55. What do noinline and crossinline do?

**Answer:** noinline keeps one lambda as a real function value so it may be stored or passed normally. crossinline keeps a lambda inlinable but forbids non-local return, which is necessary when the lambda is invoked from another context.

### 56. What is a reified type parameter?

**Answer:** In an inline function, reified makes the actual type available at the call site for checks, casts, or class literals. Normal JVM generic type parameters are erased.

~~~kotlin
inline fun <reified T> Any?.isType(): Boolean = this is T
~~~

### 57. What does tailrec do?

**Answer:** It asks the compiler to turn an eligible tail-recursive function into a loop, avoiding stack growth. The recursive call must be the final operation on every recursive path; otherwise the compiler warns that optimization was not applied.

### 58. What is operator fun invoke?

**Answer:** It lets an object use call syntax. It is common for use-case objects because getUser(id) reads naturally while the object can still hold dependencies.

### 59. What is an infix function?

**Answer:** A member or extension with one value parameter can be marked infix and called without dots or parentheses, such as 1 to 2. Use it only when the expression remains unambiguous and reads like domain language.

### 60. What is a SAM or fun interface?

**Answer:** A single-abstract-method interface has exactly one abstract method and can be constructed with a lambda. Kotlin supports Java SAM types and explicitly declared Kotlin fun interfaces.

### 61. What are callable references?

**Answer:** The :: syntax creates references to functions, constructors, or properties, such as ::println, ::User, and User::name. They can be passed wherever the corresponding function type is expected.

### 62. What is local versus non-local return from a lambda?

**Answer:** return@label exits only the labeled lambda. An unqualified return from a lambda passed to an eligible inline function can exit the enclosing function. Anonymous functions use local return semantics.

### 63. What is a closure?

**Answer:** A lambda or local function that captures variables from its surrounding scope. Capturing mutable state can complicate concurrency and object lifetime, so keep captured state small and controlled.

### 64. What is property delegation?

**Answer:** A delegated property forwards getValue and optionally setValue to another object. Standard delegates include lazy, observable, and map-backed properties. Delegation centralizes reusable property behavior.

### 65. What is the difference between throw and return types in Kotlin?

**Answer:** throw is an expression of type Nothing, so it can appear wherever a value is expected. return transfers control from a function and also has type Nothing in the expression being analyzed.

## OOP and object design

### 66. What are the four pillars of OOP?

**Answer:** Encapsulation protects state behind a controlled API. Abstraction exposes essential behavior while hiding detail. Inheritance models a valid is-a relationship. Polymorphism lets callers use one abstraction while implementations vary.

### 67. What is encapsulation in a real example?

**Answer:** Keep mutable balance private and expose operations that preserve rules. A public mutable balance lets callers bypass validation and create impossible state.

~~~kotlin
class BankAccount(initialBalance: Long) {
    var balance: Long = initialBalance
        private set

    fun withdraw(amount: Long) {
        require(amount > 0 && amount <= balance)
        balance -= amount
    }
}
~~~

### 68. What is abstraction in a real example?

**Answer:** UserRepository exposes domain operations such as observeUser and refresh without exposing whether data comes from Room, Retrofit, or a fake. Callers depend on what the system does, not storage details.

### 69. What is polymorphism in a real example?

**Answer:** Code accepts an interface and invokes the same operation on different implementations. A checkout service can call processor.pay on card, wallet, or fake processors without branching on their concrete classes.

### 70. When is composition better than inheritance?

**Answer:** Prefer composition when behaviors vary independently or the relationship is has-a rather than is-a. A SyncManager has a UserRepository; it is not a repository. Composition lowers coupling and makes testing or replacement easier.

### 71. What are association, aggregation, and composition?

**Answer:** Association is a general link between objects. Aggregation is weak ownership where parts can exist independently, such as Team and Player. Composition is strong ownership/lifetime coupling, such as an Order owning its LineItems.

### 72. What are cohesion and coupling?

**Answer:** High cohesion means a component's responsibilities belong together. Low coupling means it knows little about other components' details. Good designs generally seek high cohesion and low coupling, without creating layers that only forward calls.

### 73. What contract must equals and hashCode follow?

**Answer:** Equality should be reflexive, symmetric, transitive, consistent, and false for null. Equal objects must have equal hash codes. Unequal objects may collide. Fields used by equals should also contribute consistently to hashCode.

### 74. Why are mutable HashMap keys dangerous?

**Answer:** If a field used by equals or hashCode changes after insertion, the key may now belong to a different bucket and lookup can fail. Prefer immutable key objects.

### 75. What is the difference between shallow and deep copying?

**Answer:** A shallow copy duplicates the outer object but shares referenced children. A deep copy recursively duplicates the relevant mutable graph. Kotlin data-class copy is shallow.

## SOLID with practical Kotlin examples

### 76. What is the Single Responsibility Principle?

**Answer:** A component should have one cohesive responsibility-more precisely, one main reason to change. A ViewModel that fetches Retrofit data, parses JSON, writes SQL, formats dates, and renders text has many unrelated reasons to change.

~~~kotlin
// Focused responsibilities:
class UserRepository(
    private val api: UserApi,
    private val dao: UserDao,
) {
    suspend fun refresh() {
        dao.replaceAll(api.loadUsers().map(UserDto::toEntity))
    }
}

class UserViewModel(
    private val repository: UserRepository,
) : ViewModel() {
    // Owns screen state and user events, not HTTP or SQL details.
}
~~~

### 77. Does SRP mean every class should contain one function?

**Answer:** No. Several functions may support one cohesive responsibility. Splitting every method into another class creates navigation overhead and an anemic design. Separate code when responsibilities change for different reasons or need independent reuse/testing.

### 78. What is the Open/Closed Principle?

**Answer:** Software should be open to extension but closed to risky modification. Put genuine variation behind an abstraction so adding a new behavior does not require editing a central chain of unrelated conditions.

~~~kotlin
fun interface DiscountPolicy {
    fun priceInCents(original: Long): Long
}

class Checkout(private val policy: DiscountPolicy) {
    fun totalInCents(original: Long): Long = policy.priceInCents(original)
}

val studentDiscount = DiscountPolicy { price -> price * 80 / 100 }
val regularPrice = DiscountPolicy { price -> price }
~~~

### 79. Does OCP forbid modifying existing code?

**Answer:** No. Bugs, changing requirements, and refactoring require modification. OCP says stable code should expose intentional extension points where variation is expected. Do not create an interface for every class based on imaginary future changes.

### 80. What is the Liskov Substitution Principle?

**Answer:** A subtype must be usable wherever its base type is expected without breaking the base contract. It must not strengthen preconditions, weaken promised results, or introduce surprising unsupported behavior.

~~~kotlin
// Bad: a read-only cache would have to throw from put().
interface Cache {
    fun get(key: String): String?
    fun put(key: String, value: String)
}

// Better contracts:
interface ReadableCache {
    fun get(key: String): String?
}

interface WritableCache : ReadableCache {
    fun put(key: String, value: String)
}
~~~

### 81. How do you spot an LSP violation?

**Answer:** Look for subtype methods that throw UnsupportedOperationException, require inputs the parent allowed, return weaker results, or force callers to check concrete types. Such checks often mean the abstraction promises too much.

### 82. What is the Interface Segregation Principle?

**Answer:** Clients should not depend on methods they do not use. Prefer small role-based interfaces over a large interface that forces implementations to provide meaningless methods.

~~~kotlin
// Instead of one giant listener:
fun interface ItemClickListener {
    fun onItemClick(id: Long)
}

fun interface RetryListener {
    fun onRetry()
}

@Composable
fun ErrorContent(onRetry: RetryListener) { /* ... */ }
~~~

### 83. Does ISP mean one-method interfaces everywhere?

**Answer:** No. An interface may contain several operations when clients need them as one cohesive contract. Split it when different consumers use independent subsets or implementations are forced to fake unsupported behavior.

### 84. What is the Dependency Inversion Principle?

**Answer:** High-level policy should not depend directly on low-level details; both should depend on abstractions owned around the policy boundary. A ViewModel should depend on a repository contract, not construct Retrofit or Room itself.

~~~kotlin
interface UserRepository {
    suspend fun user(id: Long): User
}

class ProfileViewModel(
    private val users: UserRepository,
) : ViewModel()

class NetworkUserRepository(
    private val api: UserApi,
) : UserRepository {
    override suspend fun user(id: Long): User = api.user(id).toDomain()
}
~~~

### 85. What is the difference between dependency inversion and dependency injection?

**Answer:** Dependency inversion is a design principle about the direction of dependencies. Dependency injection is a construction technique that supplies dependencies from outside. DI can inject a concrete implementation and still violate DIP; using an interface without injecting it can still hide construction problems.

### 86. Can SOLID be overused?

**Answer:** Yes. Too many interfaces, one-line use cases, factories, and layers increase cognitive load without adding flexibility. Apply SOLID to real change axes, contracts, and test seams-not as a class-count target.

## Generics and collections

### 87. What do out and in mean in generic variance?

**Answer:** out marks a producer: values come out as T, enabling covariance. in marks a consumer: values go in as T, enabling contravariance. A common memory aid is PECS: producer extends/out, consumer super/in.

### 88. What is use-site variance?

**Answer:** It applies a projection where a generic type is used rather than on its declaration, such as MutableList<out Animal>. It restricts unsafe operations for that reference while preserving safe reads.

### 89. What is a star projection?

**Answer:** Foo<*> means Foo of some unknown type while retaining the operations that are safe without knowing that type. It is safer than erasing the constraint to Any?.

### 90. What is type erasure?

**Answer:** On the JVM, most generic type arguments are not present at runtime. You can normally check value is List<*>, but not value is List<String>. Inline reified functions can use the call-site type for some operations but do not magically preserve every nested generic argument.

### 91. What is the difference between List and MutableList?

**Answer:** List exposes read-only operations; MutableList adds mutation. The read-only interface is not a deep immutability guarantee because another reference may mutate the same underlying collection.

### 92. ArrayList or LinkedList?

**Answer:** ArrayList provides O(1) indexed access, amortized O(1) append, compact storage, and good cache locality. LinkedList has O(n) indexed access and only gives O(1) insertion/removal when you already have the node or iterator position. ArrayList is usually the better JVM default.

### 93. How does HashMap provide average O(1) lookup?

**Answer:** It hashes a key to choose a bucket and then uses equality to find the key inside that bucket. Collisions are allowed. Poor hash distribution or adversarial collisions can degrade performance, and resizing is occasionally expensive.

### 94. What is the difference between map and flatMap?

**Answer:** map transforms each input into one output. flatMap transforms each input into an iterable/sequence and flattens the results. For nullable values, mapNotNull is often clearer than mapping and then filtering nulls.

### 95. What is the difference between fold and reduce?

**Answer:** fold starts with an explicit accumulator and works on an empty collection. reduce uses the first element as the initial accumulator and fails on an empty collection. fold can also produce a result type different from the element type.

### 96. Iterable operations or Sequence operations?

**Answer:** Normal collection chains are eager and may create intermediate collections. Sequence chains are lazy and can avoid intermediates or stop early. Sequences add overhead, so they are not automatically faster for small collections or short pipelines.

## General coding and CS questions

### 97. What is Big O, and what should you say in an interview?

**Answer:** Big O describes how time or space grows as input grows, ignoring constant factors and lower-order terms. State the relevant average/worst case, identify n, include extra space, and mention important assumptions such as hash quality or sorted input.

### 98. Stack versus queue?

**Answer:** A stack is LIFO and fits backtracking, parsing, and undo. A queue is FIFO and fits ordered work and breadth-first search. Kotlin's ArrayDeque efficiently supports both ends and is generally preferable to the old java.util.Stack.

### 99. What is the difference between volatile, atomic operations, and a lock?

**Answer:** volatile gives visibility and ordering for individual reads/writes but does not make compound actions such as count++ atomic. Atomic types provide lock-free atomic updates for simple state. A lock or Mutex protects multi-step invariants; Mutex is suitable when the critical section may suspend.

### 100. What makes code easy to test?

**Answer:** Deterministic behavior, explicit inputs/outputs, injected dependencies, small cohesive units, controlled time/dispatchers, and minimal global mutable state. Test behavior through public contracts. A fake is often clearer than heavy mocking when it can model the dependency simply.

## Rapid-fire checklist

- State the difference first, then give one example and one pitfall.
- For collections, mention both complexity and real JVM tradeoffs.
- For inheritance, explain the contract and substitutability-not only syntax.
- For SOLID, name the concrete reason the design changes or becomes easier to test.
- For Kotlin features, say whether behavior is compile-time, runtime, static dispatch, or dynamic dispatch.
- If a question is ambiguous, state the assumption before answering.
