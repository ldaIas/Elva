package net.elva.tooling

import java.io.File
import net.elva.lang.parser.ElvaLexer

fun main(args: Array<String>) {
    
    if (args.isEmpty()) {
        printUsage()
        return
    }

    val file = File(args[0])
    if (!file.exists()) {
        println("Requested source file ${file.absolutePath} does not exist")
        return
    }

    val source = file.readText()

    println("=== Tokenizing ${file.name} ===")
    val lexer = ElvaLexer(source)
    val tokens = lexer.tokenize()
    for (token in tokens) {
        println(token)
    }
    println("=== Done ===")
}

fun printUsage() {
    println("Usage: elva <source file>")
}