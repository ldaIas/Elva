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
import net.elva.lang.ast.RecordDecl
import net.elva.lang.ast.RecordField
import net.elva.lang.ast.ExprRecordCtorNamed
import net.elva.lang.ast.ExprRecordCtor
import net.elva.lang.ast.ExprFloat
import net.elva.lang.ast.ExprInt
import net.elva.lang.ast.ExprString
import net.elva.lang.ast.ExprBool
import net.elva.lang.ast.TypedefDecl
import net.elva.lang.ast.TypeExpr
import net.elva.lang.ast.TypeApplied
import net.elva.lang.ast.TypeFunc
import net.elva.lang.ast.TypeNamed
import net.elva.lang.ast.TypeVar
import net.elva.lang.ast.TypeUnit
import kotlin.check


class ElvaParser(private val tokens: List<Token>) {

    private var current = 0

    /**
     * Used to parse the entire source code into a list of top level declarations.
     * Common in actual source code parsing, but not for REPL or expression unit testing
     */
    fun parseTopLevel(): List<TopLevelDecl> {
        val declarations = mutableListOf<TopLevelDecl>()

        while (!isAtEnd()) {
            when (peek().type) {
                TokenType.MSG -> declarations.add(parseMsg())
                TokenType.FN -> declarations.add(parseFn())
                TokenType.RECORD -> declarations.add(parseRecord())
                TokenType.TYPEDEF -> declarations.add(parseTypedef())
                TokenType.EOF -> break
                else -> error("Parse error at ${peek()}: Expected top level declaration.\nTo parse single expressions at the top level, use `$ elva <source> true`")
            }
        }
        return declarations
    }

    /**
     * Used to parse "top level expressions" - expressions not used in a fun or identifier.
     * Common in REPL cases or for unit testing expression evaluation, but not for actual source code
     */
    fun parseExprTopLevel(): Expr {
        return parseExpr()
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

    private fun parseTypeExpr(): TypeExpr {
        var type = parseTypePrimary()

        // Handle chained type application: List A B -> ((List A) B)
        while (!isAtEnd() && (check(TokenType.IDENTIFIER) || check(TokenType.LPAREN))) {
            val arg = parseTypePrimary()
            type = TypeApplied(type, arg)
        }

        return type
    }

    private fun parseTypePrimary(): TypeExpr {
        return when {
            // Either (), (TypeExpr), or (TypeExpr |-> TypeExpr)
            match(TokenType.LPAREN) -> {

                if (check(TokenType.RPAREN)) {
                    advance()
                    TypeUnit
                }

                val from = parseTypeExpr()
                if (match(TokenType.FN_ARROW)) {
                    val to = parseTypeExpr()
                    consume(TokenType.RPAREN, "Expected ')' after function type")
                    TypeFunc(from, to)
                } else {
                    consume(TokenType.RPAREN, "Expected ')' after type expression")
                    from
                }
            }

            // Either a named type (String) or type var (T)
            // Compiler enforces defined type names must be greater than 1 char and not all caps, and type variables must be all caps
            check(TokenType.IDENTIFIER) -> {
                val name = advance().lexeme
                if (name.all { it.isUpperCase() }) {
                    TypeVar(name)
                } else {
                    TypeNamed(name)
                }

            }

            else -> error("Parse error at ${peek()}: Expected type expression")
        }
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
                val paramType = parseTypeExpr()
                fnParams.add(FnParam(paramName, paramType))
            } while (match(TokenType.COMMA))
            
        }

        consume(TokenType.RPAREN, "Expected ')' after function parameters")

        consume(TokenType.FN_ARROW, "Expected |-> after terminating the input types with ')' in function definition")

        // Collect the function types
        val fnReturnTypes = mutableListOf<TypeExpr>()
        consume(TokenType.LPAREN, "Expected '(' before function return type(s)")
        do {
            val type = parseTypeExpr()
            fnReturnTypes.add(type)
        } while (match(TokenType.COMMA))
        val fnReturnType = FnReturnType(fnReturnTypes)


        consume(TokenType.RPAREN, "Expected ')' after function return type(s)")
        consume(TokenType.EQUAL, "Expected '=' after terminating the return types with ')' in function definition")

        val body = parseExpr()

        return FnDecl(fnName, fnParams, fnReturnType, body)
    }

    /**
     * Parse record definitions.
     * For example:
     * record MyRecord
     *    = { val1: Int
     *      , val2: String
     *      }
     */
    private fun parseRecord(): RecordDecl {
        consume(TokenType.RECORD, "Expected `record`")
        val name = consume(TokenType.IDENTIFIER, "Expected record name").lexeme
        consume(TokenType.EQUAL, "Expected '=' after record name")
        consume(TokenType.LBRACE, "Expected '{' before record fields")

        val fields = mutableListOf<RecordField>()
        while (!check(TokenType.RBRACE)) {
            val fieldName = consume(TokenType.IDENTIFIER, "Expected field name").lexeme
            consume(TokenType.COLON, "Expected ':' after field name")
            val fieldType = parseTypeExpr()
            fields.add(RecordField(fieldName, fieldType))

            if (!check(TokenType.RBRACE)) {
                consume(TokenType.COMMA, "Expected ',' or '}' after field")
            }
        }

        consume(TokenType.RBRACE, "Expected '}' after record fields")
        return RecordDecl(name, fields)
    }

    /**
     * Parses meta-record typedefs.
     * For example:
     * typedef surface <: (V: record, E: msg)
     *     = { view: V
     *       , effects: E
     *       }
     */
    private fun parseTypedef(): TopLevelDecl {

        consume(TokenType.TYPEDEF, "Expected 'typedef'")

        val typedefName = consume(TokenType.IDENTIFIER, "Expected typedef name").lexeme

        // Parse optional "<: (K: kind, ...)" after typedef name
        val typeParams = mutableListOf<Pair<String, TypeExpr>>()
        if (match(TokenType.TYPE_CONST)) {
            consume(TokenType.LPAREN, "Expected '(' after type parameters")

            do {
                val paramName = consume(TokenType.IDENTIFIER, "Expected type parameter name").lexeme
                consume(TokenType.COLON, "Expected ':' after type parameter name")
                val paramType = parseTypeExpr()
                typeParams.add(Pair(paramName, paramType))
            } while (match(TokenType.COMMA))
            consume(TokenType.RPAREN, "Expected ')' after type parameters")
        }

        consume(TokenType.EQUAL, "Expected '=' after typedef header")
        consume(TokenType.LBRACE, "Expected '{' before typedef record fields")

        val fields = mutableListOf<RecordField>()
        while (!check(TokenType.RBRACE)) {
            val fieldName = consume(TokenType.IDENTIFIER, "Expected field name").lexeme
            consume(TokenType.COLON, "Expected ':' after field name")
            val fieldType = parseTypeExpr()
            fields.add(RecordField(fieldName, fieldType))

            if (!check(TokenType.RBRACE)) {
                consume(TokenType.COMMA, "Expected ', ' or '}' after field in typedef shape definition")
            }
        }

        consume(TokenType.RBRACE, "Expected '}' after typedef shape definition")
        return TypedefDecl(typedefName, typeParams, fields)
    }

    private fun parseExpr(): Expr {
        println(peek())
        return when {

            // Literals
            check(TokenType.NUMBER) -> parseNumber()
            check(TokenType.STRING) -> parseString()
            check(TokenType.TRUE) -> parseBoolean(true)
            check(TokenType.FALSE) -> parseBoolean(false)

            // '(' - Either a unit expression ("()") or a paranthesized expression ("(expr)")
            match(TokenType.LPAREN) -> {
                if (check(TokenType.RPAREN)) {
                    advance()
                    ExprUnit
                } else {
                    val inner = parseExpr()
                    consume(TokenType.RPAREN, "Expected ')' after expression")
                    ExprParens(inner)
                }
            }

            // Handle match cases
            match(TokenType.MATCH) -> parseMatch()

            // Some identifier (var, fun, record, etc)
            check(TokenType.IDENTIFIER) -> {
                val identifier = advance()
                val name = identifier.lexeme
                return when {
                    // Named valued record construction (MyRecord {val1 = x, val2 = y, ...}) or shothand default construction (MyRecord {})
                    check(TokenType.LBRACE) -> parseNamedRecordCtor(name)

                    // Shorthand valued record construction (MyRecord x y)
                    check(TokenType.IDENTIFIER) || check(TokenType.NUMBER) || check(TokenType.STRING) || check(TokenType.LPAREN) -> {
                        val args = mutableListOf<Expr>()
                        while (check(TokenType.IDENTIFIER) || check(TokenType.NUMBER) || check(TokenType.STRING) || check(TokenType.LPAREN)) {
                            args.add(parseExpr())
                        }
                        ExprRecordCtor(name, args)
                    }

                    else -> ExprVar(name) // Could be a lot of things if its just an identifier
                }
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
    

    /**
     * Matches the given token type and consumes it if it matches.
     * Returns true if the token type matches, false otherwise.
     */
    private fun match(type: TokenType): Boolean {
        if (check(type)) {
            advance()
            return true
        }
        return false
    }

    private fun parseNumber(): Expr {
        val token = advance()
        val value = token.lexeme

        return if (value.contains('.')) {
            ExprFloat(value.toDouble())
        } else {
            ExprInt(value.toInt())
        }
    }

    private fun parseBoolean(value: Boolean): Expr { 
        advance()
        return ExprBool(value)
    }

    private fun parseString(): Expr {
        val token = advance()

        // Remove the "" surrounding the text value that is the string
        return ExprString(token.lexeme.removeSurrounding("\""))
    }

    private fun parseNamedRecordCtor(name: String): ExprRecordCtorNamed {
        consume(TokenType.LBRACE, "Expected '{' before record field names")

        val fields = mutableListOf<Pair<String, Expr>>()
        while (!check(TokenType.RBRACE)) {
            val fieldName = consume(TokenType.IDENTIFIER, "Expected record field name").lexeme
            consume(TokenType.EQUAL, "Expected '=' after field name")
            val fieldValue = parseExpr()
            fields.add(Pair(fieldName, fieldValue))

            if (!check(TokenType.RBRACE)) {
                consume(TokenType.COMMA, "Expected field seperation (',') or construction termination ('}') after field")
            }
            
        }

        consume(TokenType.RBRACE, "Expected '}' after record field names")
        return ExprRecordCtorNamed(name, fields)
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