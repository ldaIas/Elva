package net.elva.parser

import net.elva.lang.parser.ElvaLexer
import net.elva.lang.parser.ElvaParser
import net.elva.lang.ast.ExprRecordCtorNamed
import net.elva.lang.ast.ExprString
import net.elva.lang.ast.Expr
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ElvaParserTests {

    /**
     * Test to ensure that a simple named valued record constructor is parsed correctly with one string field.
     * I.e. the following:
     * CounterModel { count = "abc"}
     */
    @Test
    fun `Test to parse simple named valued record constructor with string field`() {
        val sourceCode = 
        """\
        CounterModel { name = "abc" }
        """

        val expr = parseExpr(sourceCode)

        val expected = ExprRecordCtorNamed(
            name = "CounterModel",
            args = listOf(
                Pair("name", ExprString("abc"))
            )
        )

        assertEquals(expected, expr, "Expected parsed AST to match expected AST")
    }

    fun parseExpr(source: String): Expr {
        val tokens = ElvaLexer(source).tokenize()
        return ElvaParser(tokens).parseExprTopLevel()
    }

}