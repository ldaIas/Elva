package net.elva.tooling

import java.io.File
import net.elva.lang.parser.ElvaLexer
import net.elva.lang.parser.ElvaParser

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

    println("\n=== Parsing ${file.name} ===")
    val parser = ElvaParser(tokens)
    val decls = parser.parseTopLevel()
    println("=== Done ===")

    println("\n=== AST ===")
    decls.forEach(::println)
}

fun printUsage() {
    println("Usage: elva <source file>")
}