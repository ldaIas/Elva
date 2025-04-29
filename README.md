# Elva

**Elva** is a new programming language inspired by Elm and the Java ecosystem, designed for purity, simplicity, and powerful effect management. It aims to bring modern functional programming ideas to the JVM world with an easy-to-use, reliable runtime.

---

## Project Goals

- **Purity First**: Every function is pure by default. No hidden side effects.
- **Explicit Effects**: All side effects are modeled and managed through a clean effect system.
- **Strong Static Typing**: No `null`. Clear, enforced types.
- **Lazy Effect Execution**: Effects are handled lazily by the runtime.
- **Simple, Practical Syntax**: Inspired by Elm, but adapted for general-purpose use.
- **Seamless Java Interop**: Designed to easily call Java libraries when needed.

---

## Core Concepts

- **Purpose**: Represents the application's state (similar to Elm's `Model`).
- **View**: Defines how the Purpose is presented to the user (CLI, GUI, etc.).
- **Update**: Handles `Messages` to transform the Purpose.
- **Effect Machine**: Effects are described, not performed directly. The runtime manages them.
- **Superpose**: Special type to handle multiple superposed instances, opening paths for innovative concurrent designs.

---

## Example (Proposed Syntax)

```elva
purpose Counter {
    value: Int
}

view counter -> Text {
    "Current count: " ++ counter.value.toString()
}

update counter msg =
    case msg of
        Increment -> { counter | value = counter.value + 1 }
        Decrement -> { counter | value = counter.value - 1 }
        _ -> counter

main =
    start Counter
```

---

## Status

🚧 **Work in Progress** 🚧

Elva is currently in early development.  
We're starting with a basic interpreter/runtime in Kotlin targeting the JVM, focusing on file I/O, console output, and pure update/message loops.

---

## Tech Stack

- **Kotlin** - Language for the initial implementation
- **Gradle** - Build and dependency management
- **JVM** - Target runtime

---

## Contributing

Contributions are welcome!  
More details and a roadmap will be posted once the core runtime is stable.

If you're interested early, feel free to open an issue or a discussion.

---

## License

TBD (probably MIT or Apache 2.0)

---

## Author

Built with love by Sam Sovereign and collaborators.

---

## Related Inspirations

- [Elm Language](https://elm-lang.org/)
- [Kotlin](https://kotlinlang.org/)
- [Effect Systems research](https://en.wikipedia.org/wiki/Effect_system)

---

## Roadmap

- [ ] Basic Purpose/View/Update runtime
- [ ] Simple effect system with Console I/O
- [ ] JVM interop
- [ ] Example applications
- [ ] Package manager design
- [ ] Explore Superpose capabilities

---

> **Elva**: Simple, safe, and purposeful programming.
