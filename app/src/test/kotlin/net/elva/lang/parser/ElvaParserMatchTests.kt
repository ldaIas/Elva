package net.elva.lang.parser

import net.elva.lang.ast.ExprMatch
import net.elva.lang.ast.ExprUnit
import net.elva.lang.ast.ExprVar
import net.elva.lang.ast.MatchBranch
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import net.elva.lang.parser.ElvaParserTestUtils.parseExpr

/**
 * Tests for parsing match expressions with the Elva Parser
 */
class ElvaParserMatchTests {

    @Test
    fun `Test to parse simple match expression`() {
        val sourceCode =
            """\
        match x:
            Inc -> ()
            Dec -> ()
            _ -> ()
        """

        val expr = parseExpr(sourceCode)

        val expected =
            ExprMatch(
                target = ExprVar("x"),
                branches =
                    listOf(
                        MatchBranch("Inc", ExprUnit),
                        MatchBranch("Dec", ExprUnit),
                        MatchBranch("_", ExprUnit)
                    )
            )

        assertEquals(expected, expr, "Expected parsed AST to match expected AST")
    }
}