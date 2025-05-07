package net.elva.lang.parser

import kotlin.test.assertEquals
import net.elva.lang.ast.*
import net.elva.lang.parser.ElvaParserTestUtils.parseExpr
import org.junit.jupiter.api.Test

/**
 * Tests for parsing records with the Elva Parser
 */
class ElvaParserRecordTests {

    /**
     * Test to ensure that a simple named valued record constructor is parsed correctly with one
     * string field. I.e. the following: CounterModel { name = "abc"}
     */
    @Test
    fun `Test to parse simple named valued record constructor with string field`() {
        val sourceCode = """\
        CounterModel { name = "abc" }
        """

        val expr = parseExpr(sourceCode)

        val expected =
                ExprRecordCtorNamed(
                        name = "CounterModel",
                        args = listOf(Pair("name", ExprString("abc")))
                )

        assertEquals(expected, expr, "Expected parsed AST to match expected AST")
    }

    /**
     * Test to ensure that a simple named valued record constructor is parsed correctly with
     * multiple fields of varying types. I.e. the following: CounterModel { name = "abc", count =
     * 123, enabled = true }
     */
    @Test
    fun `Test to parse simple named valued record constructor with multiple fields of varying types`() {
        val sourceCode =
                """\
        CounterModel { name = "abc", count = 123, enabled = true }
        """

        val expr = parseExpr(sourceCode)

        val expected =
                ExprRecordCtorNamed(
                        name = "CounterModel",
                        args =
                                listOf(
                                        Pair("name", ExprString("abc")),
                                        Pair("count", ExprInt(123)),
                                        Pair("enabled", ExprBool(true))
                                )
                )

        assertEquals(expected, expr, "Expected parsed AST to match expected AST")
    }
}
