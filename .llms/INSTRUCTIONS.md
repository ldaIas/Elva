# Instructions

We are making a programming language that brings the concepts of the Elm language and
runtime to the JVM for general programming, with an emphasis on being "async-less".
The Elva Effect Machine handles all side-effects and sends Messages to a defined update function.
The update function can take in a Superposition of messages that will process all updates simultaneously.
No user defined "async" or "await" commands.

You can read the README.md to get a general overview of the project.

## Project structure

This is a kotlin 2.1 project. Built using the .kts gradle DSL

The files for the Elva language are located in
./app/src/main/kotlin/net/elva

I've been using files in app/src/test/resources to test source file input.

There is a small CLI available to process files. Use this:
`$ sh run.sh <source file> [true|false]`
to run the CLI for now. The source file is what will be parsed and the true/false flags
are for parsing top level expressions (`true`, not production source code; usually testing or REPL) or
not (`false`, no naked expressions, expressions must be part of a top level declaration)
