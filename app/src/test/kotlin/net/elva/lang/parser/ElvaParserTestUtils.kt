package net.elva.lang.parser

import net.elva.lang.ast.Expr
import net.elva.lang.ast.TopLevelDecl
import net.elva.lang.ast.TypeExpr

object ElvaParserTestUtils {

    fun parseTypeExpr(source: String): TypeExpr {
        val tokens = ElvaLexer(source).tokenize()
        return ElvaParser(tokens).parseTypeExpr()
    }

    fun parseDecls(source: String): List<TopLevelDecl> {
        val tokens = ElvaLexer(source).tokenize()
        return ElvaParser(tokens).parseTopLevel()
    }

    fun parseExpr(source: String): Expr {
        val tokens = ElvaLexer(source).tokenize()
        return ElvaParser(tokens).parseExprTopLevel()
    }

}