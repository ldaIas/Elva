package net.elva.lang.tokens

data class Token(
    val type: TokenType,
    val lexeme: String,
    val line: TokenPosition = TokenPosition(0, 0)
) {
    override fun toString(): String {
        return "'$lexeme' (of $type) (line: ${line.line} col: ${line.column})"
    }
}

data class TokenPosition(val line: Int, val column: Int)