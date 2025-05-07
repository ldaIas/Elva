package net.elva.lang.parser

import net.elva.lang.ast.*
import net.elva.lang.parser.ElvaParserTestUtils.parseDecls
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for parsing functions with the Elva Parser
 */
class ElvaParserFunTests {

    /**
     * Test to ensure that a simple fun declaration with body is parsed correctly. Tests 1 input and
     * 1 output. I.e. the following: fn update (x: CounterModel) |-> (CounterModel) =
     * ```
     *     x
     * ```
     */
    @Test
    fun `Test to parse simple fun declaration with body, 1 input 1 output`() {
        val sourceCode =
            """\
        fn update (x: CounterModel) |-> (CounterModel) =
            x
        """

        val decls = parseDecls(sourceCode)

        val expected =
            listOf(
                FnDecl(
                    name = "update",
                    params = listOf(FnParam("x", TypeNamed("CounterModel"))),
                    returnType = FnReturnType(listOf(TypeNamed("CounterModel"))),
                    body = ExprVar("x")
                )
            )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }

    /**
     * Test to ensure that a simple fun declaration with body is parsed correctly. Tests multiple
     * inputs and outputs I.e. the following: fn update (x: CounterModel) |-> (CounterModel) =
     * ```
     *     x
     * ```
     */
    @Test
    fun `Test to parse simple fun declaration with body, several ins n outs`() {
        val sourceCode =
            """\
        fn fiveIntToThreeInt (x1: Int, x2: Int, x3: Int, x4: Int, x5: Int) |-> (Int, Int, Int) =
            (x1)
        """

        val decls = parseDecls(sourceCode)

        val expected =
            listOf(
                FnDecl(
                    name = "fiveIntToThreeInt",
                    params =
                        listOf(
                            FnParam("x1", TypeNamed("Int")),
                            FnParam("x2", TypeNamed("Int")),
                            FnParam("x3", TypeNamed("Int")),
                            FnParam("x4", TypeNamed("Int")),
                            FnParam("x5", TypeNamed("Int"))
                        ),
                    returnType =
                        FnReturnType(
                            listOf(
                                TypeNamed("Int"),
                                TypeNamed("Int"),
                                TypeNamed("Int")
                            )
                        ),
                    body = ExprParens(ExprVar("x1"))
                )
            )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }
}