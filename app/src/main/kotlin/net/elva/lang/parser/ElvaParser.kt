package net.elva.lang.parser

import net.elva.lang.tokens.Token
import net.elva.lang.tokens.TokenType
import net.elva.lang.ast.TopLevelDecl
import net.elva.lang.ast.MsgDecl
import net.elva.lang.ast.MsgVariant
import net.elva.lang.ast.FnDecl
import net.elva.lang.ast.FnParam
import net.elva.lang.ast.FnReturnType
import net.elva.lang.ast.ExprUnit
import net.elva.lang.ast.ExprParens
import net.elva.lang.ast.ExprVar
import net.elva.lang.ast.Expr
import net.elva.lang.ast.ExprMatch
import net.elva.lang.ast.MatchBranch


class ElvaParser(private val tokens: List<Token>) {

    private var current = 0

    fun parseTopLevel(): List<TopLevelDecl> {
        val declarations = mutableListOf<TopLevelDecl>()

        while (!isAtEnd()) {
            when (peek().type) {
                TokenType.MSG -> declarations.add(parseMsg())
                TokenType.FN -> declarations.add(parseFn())
                TokenType.EOF -> break
                else -> error("Parse error at ${peek()}: Expected top level declaration.")
            }
        }
        return declarations
    }

    private fun parseMsg(): MsgDecl {
        consume(TokenType.MSG, "Expected 'msg'")

        val name = consume(TokenType.IDENTIFIER, "Expected message type name").lexeme
        consume(TokenType.EQUAL, "Expected '=' after message type name")

        val variants = mutableListOf<MsgVariant>()
        variants.add(MsgVariant(consume(TokenType.IDENTIFIER, "Expected variant name").lexeme))

        while (match(TokenType.PIPE)) {
            val variantName = consume(TokenType.IDENTIFIER, "Expected variant name after '|'").lexeme
            variants.add(MsgVariant(variantName))
        }

        return MsgDecl(name, variants)
    }

    private fun parseFn(): FnDecl {
        consume(TokenType.FN, "Expected 'fn'")

        val fnName = consume(TokenType.IDENTIFIER, "Expected function name").lexeme

        // Collect the function parameters
        consume(TokenType.LPAREN, "Expected '(' before function parameters")

        val fnParams = mutableListOf<FnParam>()
        if (!check(TokenType.RPAREN)) {
            do {
                val paramName = consume(TokenType.IDENTIFIER, "Expected parameter name").lexeme
                consume(TokenType.COLON, "Expected ':' after parameter name")
                val paramType = consume(TokenType.IDENTIFIER, "Expected parameter type").lexeme
                fnParams.add(FnParam(paramName, paramType))
            } while (match(TokenType.COMMA))
            
        }

        consume(TokenType.RPAREN, "Expected ')' after function parameters")

        consume(TokenType.FN_ARROW, "Expected |-> after terminating the input types with ')' in function definition")

        // Collect the function types
        val fnReturnTypes = mutableListOf<String>()
        consume(TokenType.LPAREN, "Expected '(' before function return type(s)")
        do {
            val type = consume(TokenType.IDENTIFIER, "Expected return type").lexeme
            fnReturnTypes.add(type)
        } while (match(TokenType.COMMA))
        val fnReturnType = FnReturnType(fnReturnTypes)


        consume(TokenType.RPAREN, "Expected ')' after function return type(s)")
        consume(TokenType.EQUAL, "Expected '=' after terminating the return types with ')' in function definition")

        val body = parseExpr()

        return FnDecl(fnName, fnParams, fnReturnType, body)
    }

    private fun parseExpr(): Expr {
        return when {
            match(TokenType.LPAREN) -> {
                if (check(TokenType.RPAREN)) {
                    advance()
                    ExprUnit(peek().line)
                } else {
                    val inner = parseExpr()
                    consume(TokenType.RPAREN, "Expected ')' after expression")
                    ExprParens(inner)
                }
            }
            match(TokenType.MATCH) -> parseMatch()
            check(TokenType.IDENTIFIER) -> {
                val identifier = advance()
                ExprVar(identifier.lexeme)
            }
            else -> error("Parse error at ${peek()}: Expected expression")
        }
    }

    private fun parseMatch(): ExprMatch {
        val target = parseExpr()
        consume(TokenType.COLON, "Expected ':' after match target")

        val branches = mutableListOf<MatchBranch>()
        while (!check(TokenType.EOF) && check(TokenType.IDENTIFIER)) {
            val pattern = consume(TokenType.IDENTIFIER, "Expected pattern").lexeme
            consume(TokenType.CASE_ARROW, "Expected '->' after pattern")
            val result = parseExpr()
            branches.add(MatchBranch(pattern, result))
        }
        
        return ExprMatch(target, branches)
    }
    

    private fun match(type: TokenType): Boolean {
        if (check(type)) {
            advance()
            return true
        }
        return false
    }

    private fun consume(type: TokenType, errorMessage: String): Token {
        if (check(type)) return advance()
        error("Parse error at ${peek()}: $errorMessage")
    }

    /**
     * Checks if the next token is of the given type. Does not consume the token.
     */
    private fun check(type: TokenType): Boolean = !isAtEnd() && peek().type == type

    private fun advance(): Token = tokens[current++]
    private fun peek(): Token = tokens.getOrElse(current) { tokens.last() }
    private fun isAtEnd(): Boolean = peek().type == TokenType.EOF

}