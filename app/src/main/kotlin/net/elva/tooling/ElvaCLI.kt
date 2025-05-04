package net.elva.tooling

import java.io.File
import net.elva.lang.parser.ElvaLexer
import net.elva.lang.parser.ElvaParser
import net.elva.lang.ast.TopLevelDecl
import net.elva.lang.ast.Expr

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

    val parseTopLevelExpressions: Boolean
    if (args.size > 1) {

        val parseInput = args[1].lowercase()
        if (parseInput != "true" && parseInput != "false") {
            println("Invalid argument: $parseInput")
            printUsage()
            return
        }

        parseTopLevelExpressions = args[1].toBoolean()

    } else {
        parseTopLevelExpressions = false
    }

    val source = file.readText()

    println("=== Source ===")
    println(source)
    println("=== === ===")

    println("=== Tokenizing ${file.name} ===")
    val lexer = ElvaLexer(source)
    val tokens = lexer.tokenize()
    for (token in tokens) {
        println(token)
    }
    println("=== Done ===")

    println("\n=== Parsing ${file.name} ===")
    val parser = ElvaParser(tokens)
    val decls: List<Any>
    if (parseTopLevelExpressions) {
        println("Parsing using top level expression mode")
        decls = mutableListOf<Expr>()
        decls.add(parser.parseExprTopLevel())
    } else {
        decls = parser.parseTopLevel()
    }
    println("=== Done ===")

    println("\n=== AST ===")
    decls.forEach(::println)
}

fun printUsage() {
    println("Usage: elva <source file> [true|false - Top Level Expression Parsing]")
}