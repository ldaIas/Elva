package net.elva.lang.parser

import net.elva.lang.tokens.Token
import net.elva.lang.tokens.TokenPosition
import net.elva.lang.tokens.TokenType

class ElvaLexer(private val source: String) {
    private var current = 0
    private var line = 1
    private var column = 1

    fun tokenize(): List<Token> {
        val tokens = mutableListOf<Token>()

        while (!isAtEnd()) {
            val start = current
            val ch = advance()

            when (ch) {
                '(' -> tokens.add(token(TokenType.LPAREN, "("))
                ')' -> tokens.add(token(TokenType.RPAREN, ")"))
                '{' -> tokens.add(token(TokenType.LBRACE, "{"))
                '}' -> tokens.add(token(TokenType.RBRACE, "}"))
                ',' -> tokens.add(token(TokenType.COMMA, ","))
                '.' -> tokens.add(token(TokenType.DOT, "."))
                ':' -> tokens.add(token(TokenType.COLON, ":",))
                '=' -> tokens.add(token(TokenType.EQUAL, "="))
                '-' -> tokens.add(token(TokenType.MINUS, "-"))
                '+' -> tokens.add(token(TokenType.PLUS, "+"))
                '/' -> tokens.add(token(TokenType.SLASH, "/"))
                '|' -> {
                    if (match('-') && match('>')) {
                        tokens.add(token(TokenType.ARROW, "|->"))
                    } else {
                        tokens.add(token(TokenType.PIPE, "|"))
                    }
                }
                ' ', '\r', '\t' -> advanceColumn()
                '\n' -> {
                    line++
                    column = 1
                }

                else -> {
                    if (isAlpha(ch)) {
                        tokens.add(identifierOrKeyword(start))
                    } else if (isDigit(ch)) {
                        tokens.add(number(start))
                    }
                }
            }
        }

        tokens.add(token(TokenType.EOF, ""))
        return tokens
    }

    private fun advance(): Char {
        column++
        return source[current++]
    }
    
    private fun match(expected: Char): Boolean {
        if (isAtEnd()) return false
        if (source[current] != expected) return false

        current++; column++
        return true
    }

    private fun identifierOrKeyword(start: Int): Token {
        while (peek().isLetterOrDigit()) advance()
        val text = source.substring(start, current)
        val type = when (text) {
            "fn" -> TokenType.FN
            "Purpose" -> TokenType.PURPOSE
            "msg" -> TokenType.MSG
            else -> TokenType.IDENTIFIER
        }
        return token(type, text)
    }

    private fun number(start: Int): Token {
        while (peek().isDigit()) advance()
        val text = source.substring(start, current)
        return token(TokenType.NUMBER, text)
    }

    private fun peek(): Char = if (isAtEnd()) '\u0000' else source[current]

    private fun isAlpha(ch: Char): Boolean = ch.isLetter() || ch == '_'
    private fun isDigit(ch: Char): Boolean = ch.isDigit()
    
    private fun isAtEnd() = current >= source.length

    private fun token(type: TokenType, lexeme: String) =
        Token(type, lexeme, TokenPosition(line, column - lexeme.length))

    private fun advanceColumn() {
        column++
    }
}