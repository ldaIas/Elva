package net.elva.lang.parser

import kotlin.test.assertEquals
import net.elva.lang.ast.*
import org.junit.jupiter.api.Test

class ElvaParserTests {

    /* ******************** */
    /* Import Parsing Tests */
    /* ******************** */

    @Test
    fun `Test to parse single import statement`() {
        val sourceCode = 
        """\
        import elva.std.iofx using Debug
        """

        val decls = parseDecls(sourceCode)
        val expected =
                listOf(
                        ImportDecl(
                                pkgName = "elva.std.iofx",
                                imports = listOf(ImportItem("Debug", null))
                        )
                )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }

    @Test
    fun `Test to parse multiple imports in one statement`() {
        val sourceCode = 
        """\
        import elva.std.iofx using Debug, SimpleShellOut
        """

        val decls = parseDecls(sourceCode)
        val expected =
                listOf(
                        ImportDecl(
                                pkgName = "elva.std.iofx",
                                imports =
                                        listOf(
                                                ImportItem("Debug", null),
                                                ImportItem("SimpleShellOut", null)
                                        )
                        )
                )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }

    @Test
    fun `Test to parse imports with aliases`() {
        val sourceCode =
        """\
        import elva.std.iofx using Debug as Dbg, SimpleShellOut as Out
        """

        val decls = parseDecls(sourceCode)
        val expected =
                listOf(
                        ImportDecl(
                                pkgName = "elva.std.iofx",
                                imports =
                                        listOf(
                                                ImportItem("Debug", "Dbg"),
                                                ImportItem("SimpleShellOut", "Out")
                                        )
                        )
                )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }

    @Test
    fun `Test to parse mixed imports`() {
        val sourceCode = """\
    import elva.std.iofx using Debug, SimpleShellIn as In
    """

        val decls = parseDecls(sourceCode)
        val expected =
                listOf(
                        ImportDecl(
                                pkgName = "elva.std.iofx",
                                imports =
                                        listOf(
                                                ImportItem("Debug", null),
                                                ImportItem("SimpleShellIn", "In")
                                        )
                        )
                )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }

    /* ************************ */
    /* End Import Parsing Tests */
    /* ************************ */

    /* ********************* */
    /* Comment Parsing Tests */
    /* ********************* */

    @Test
    fun `Test to parse code with Javadoc comments`() {
        val sourceCode = 
        """\
        /**
         * This is a Javadoc comment
         */
        import elva.std.iofx using Debug
        """

        val decls = parseDecls(sourceCode)
        val expected =
            listOf(
                ImportDecl(
                    pkgName = "elva.std.iofx",
                    imports = listOf(ImportItem("Debug", null))
                )
            )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }

    @Test
    fun `Test to parse code with inline comments`() {
        val sourceCode = 
        """\
        // This is an inline comment
        import elva.std.iofx using Debug // Another comment
        """

        val decls = parseDecls(sourceCode)
        val expected =
            listOf(
                ImportDecl(
                    pkgName = "elva.std.iofx",
                    imports = listOf(ImportItem("Debug", null))
                )
            )

        assertEquals(expected, decls, "Expected parsed AST to match expected AST")
    }

    /* ************************* */
    /* End Comment Parsing Tests */
    /* ************************* */

    /* ***************** */
    /* Msg Parsing Tests */
    /* ***************** */

    /**
     * Test to ensure that a simple msg declaration is parsed correctly. Msg type variants have no
     * attributes I.e. the following: msg CounterMsg
     * ```
     *     = Inc
     *     | Dec
     *     | Invalid
     * ```
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
        val expected =
                listOf(
                        MsgDecl(
                                name = "CounterMsg",
                                variants =
                                        listOf(
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

    /* ***************************** */
    /* Type Expression Parsing Tests */
    /* ***************************** */

    /**
     * Test to ensure that a simple single type variable is parsed from the given string. I.e. the
     * following: "A"
     */
    @Test
    fun `Test to parse single char type variable`() {
        val expr = parseTypeExpr("A")
        val expected = TypeVar("A")
        assertEquals(expected, expr)
    }

    /**
     * Test to ensure that a simple single type variable is parsed from the given string. I.e. the
     * following: "MOD"
     */
    @Test
    fun `Test to parse multi char type variable`() {
        val expr = parseTypeExpr("MOD")
        val expected = TypeVar("MOD")
        assertEquals(expected, expr)
    }

    /**
     * Test to ensure that a simple type named is parsed from the given string. I.e. the following:
     * "String"
     */
    @Test
    fun `Test to parse type named`() {
        val expr = parseTypeExpr("String")
        val expected = TypeNamed("String")
        assertEquals(expected, expr)
    }

    @Test
    fun `Test to parse unit type`() {
        val expr = parseTypeExpr("()")
        val expected = TypeUnit
        assertEquals(expected, expr)
    }

    @Test
    fun `Test to parse tuple type`() {
        val expr = parseTypeExpr("(A, B)")
        val expected = TypeTuple(listOf(TypeVar("A"), TypeVar("B")))
        assertEquals(expected, expr)
    }

    @Test
    fun `Test to parse simple function type`() {
        val expr = parseTypeExpr("(A |-> B)")
        val expected = TypeFunc(TypeVar("A"), TypeVar("B"))
        assertEquals(expected, expr)
    }

    @Test
    fun `Test to parse function type with multiple args and returns`() {
        val expr = parseTypeExpr("(A, B, C |-> (A, B))")
        val expected =
                TypeFunc(
                        from = TypeTuple(listOf(TypeVar("A"), TypeVar("B"), TypeVar("C"))),
                        to = TypeTuple(listOf(TypeVar("A"), TypeVar("B")))
                )
        assertEquals(expected, expr)
    }

    @Test
    fun `Test to parse nested function type`() {
        val expr = parseTypeExpr("(A |-> (B |-> C))")
        val expected = TypeFunc(from = TypeVar("A"), to = TypeFunc(TypeVar("B"), TypeVar("C")))
        assertEquals(expected, expr)
    }

    @Test
    fun `Test to parse type application`() {
        val expr = parseTypeExpr("List A")
        val expected = TypeApplied(base = TypeNamed("List"), arg = TypeVar("A"))
        assertEquals(expected, expr)
    }

    @Test
    fun `Test to parse nested type application`() {
        val expr = parseTypeExpr("Dict String (List A)")
        val expected =
                TypeApplied(
                        base = TypeApplied(TypeNamed("Dict"), TypeNamed("String")),
                        arg = TypeApplied(TypeNamed("List"), TypeVar("A"))
                )
        assertEquals(expected, expr)
    }

    /* ********************************* */
    /* End Type Expression Parsing Tests */
    /* ********************************* */

    /* ***************** */
    /* Fun Parsing Tests */
    /* ***************** */

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

    /* *********************** */
    /* End Match Parsing Tests */
    /* *********************** */

    /* ******************** */
    /* Record Parsing Tests */
    /* ******************** */

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

    /* ************************ */
    /* End Record Parsing Tests */
    /* ************************ */

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
