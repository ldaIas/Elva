# Instructions

We are making a programming language that brings the concepts of the Elm language and
runtime to the JVM for general programming, with an emphasis on being "async-less".
The Elva Effect Machine handles all side-effects and sends Messages to a defined update function.
The update function can take in a Superposition of messages that will process all updates simultaneously.
No user defined "async" or "await" commands.
