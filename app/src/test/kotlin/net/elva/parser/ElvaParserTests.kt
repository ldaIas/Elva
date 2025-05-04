package net.elva.parser

import net.elva.lang.parser.ElvaLexer
import net.elva.lang.parser.ElvaParser
import net.elva.lang.ast.ExprRecordCtorNamed
import net.elva.lang.ast.ExprString
import net.elva.lang.ast.Expr
import net.elva.lang.ast.ExprInt
import net.elva.lang.ast.ExprBool
import net.elva.lang.ast.TopLevelDecl
import net.elva.lang.ast.MsgDecl
import net.elva.lang.ast.MsgVariant
import net.elva.lang.ast.FnDecl
import net.elva.lang.ast.FnParam
import net.elva.lang.ast.FnReturnType
import net.elva.lang.ast.ExprVar
import net.elva.lang.ast.ExprParens
import net.elva.lang.ast.ExprMatch
import net.elva.lang.ast.MatchBranch
import net.elva.lang.ast.ExprUnit
import net.elva.lang.tokens.TokenPosition
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ElvaParserTests {

    /* ***************** */
    /* Msg Parsing Tests */
    /* ***************** */

    /**
     * Test to ensure that a simple msg declaration is parsed correctly. Msg type variants have no attributes
     * I.e. the following:
     * msg CounterMsg
     *     = Inc
     *     | Dec
     *     | Invalid
     */
    @Test
    fun `Test to parse simple msg declaration`() {
        val sourceCode = 
        """\
        msg CounterMsg
            = Inc
            | Dec
            | Invalid
        """

        val decls = parseDecls(sourceCode)
        val expected = listOf(
            MsgDecl(
                name = "CounterMsg",
                variants = listOf(
                    MsgVariant("Inc"),
                    MsgVariant("Dec"),
                    MsgVariant("Invalid")
                )
            )
        )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }


    /* ********************* */
    /* End Msg Parsing Tests */
    /* ********************* */

    /* ***************** */
    /* Fun Parsing Tests */
    /* ***************** */

    /**
     * Test to ensure that a simple fun declaration with body is parsed correctly. Tests 1 input and 1 output.
     *  I.e. the following:
     * fn update (x: CounterModel) |-> (CounterModel) =
     *     x
     */
    @Test
    fun `Test to parse simple fun declaration with body, 1 input 1 output`() {
        val sourceCode =
        """\
        fn update (x: CounterModel) |-> (CounterModel) =
            x
        """

        val decls = parseDecls(sourceCode)

        val expected = listOf(
            FnDecl(
                name = "update",
                params = listOf(FnParam("x", "CounterModel")),
                returnType = FnReturnType(listOf("CounterModel")),
                body = ExprVar("x")
            
            )
        )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }

    /**
     * Test to ensure that a simple fun declaration with body is parsed correctly. Tests multiple inputs and outputs
     *  I.e. the following:
     * fn update (x: CounterModel) |-> (CounterModel) =
     *     x
     */
    @Test
    fun `Test to parse simple fun declaration with body, several ins n outs`() {
        val sourceCode =
        """\
        fn fiveIntToThreeInt (x1: Int, x2: Int, x3: Int, x4: Int, x5: Int) |-> (Int, Int, Int) =
            (x1)
        """

        val decls = parseDecls(sourceCode)

        val expected = listOf(
            FnDecl(
                name = "fiveIntToThreeInt",
                params = listOf(FnParam("x1", "Int"), FnParam("x2", "Int"), FnParam("x3", "Int"), FnParam("x4", "Int"), FnParam("x5", "Int")),
                returnType = FnReturnType(listOf("Int", "Int", "Int")),
                body = ExprParens(ExprVar("x1"))
            
            )
        )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }

    /* ********************* */
    /* End Fun Parsing Tests */
    /* ********************* */

    /* ******************* */
    /* Match Parsing Tests */
    /* ******************* */

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

        val expected = ExprMatch(
            target = ExprVar("x"),
            branches = listOf(
                MatchBranch("Inc", ExprUnit),
                MatchBranch("Dec", ExprUnit),
                MatchBranch("_", ExprUnit)
            )
        )

        assertEquals(expected, expr, "Expected parsed AST to match expected AST")
    }

    /* *********************** */
    /* End Match Parsing Tests */
    /* *********************** */

    /* ******************** */
    /* Record Parsing Tests */
    /* ******************** */

    /**
     * Test to ensure that a simple named valued record constructor is parsed correctly with one string field.
     * I.e. the following:
     * CounterModel { name = "abc"}
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

    /**
     * Test to ensure that a simple named valued record constructor is parsed correctly with multiple fields of varying types.
     * I.e. the following:
     * CounterModel { name = "abc", count = 123, enabled = true }
     */
    @Test
    fun `Test to parse simple named valued record constructor with multiple fields of varying types`() {
        val sourceCode =
        """\
        CounterModel { name = "abc", count = 123, enabled = true }
        """

        val expr = parseExpr(sourceCode)

        val expected = ExprRecordCtorNamed(
            name = "CounterModel",
            args = listOf(
                Pair("name", ExprString("abc")),
                Pair("count", ExprInt(123)),
                Pair("enabled", ExprBool(true))
            )
        )

        assertEquals(expected, expr, "Expected parsed AST to match expected AST")
    }

    /* ************************ */
    /* End Record Parsing Tests */
    /* ************************ */

    fun parseDecls(source: String): List<TopLevelDecl> {
        val tokens = ElvaLexer(source).tokenize()
        return ElvaParser(tokens).parseTopLevel()
    }

    fun parseExpr(source: String): Expr {
        val tokens = ElvaLexer(source).tokenize()
        return ElvaParser(tokens).parseExprTopLevel()
    }

}