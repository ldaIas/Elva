package net.elva.lang.parser

import net.elva.lang.ast.ImportDecl
import net.elva.lang.ast.ImportItem
import net.elva.lang.parser.ElvaParserTestUtils.parseDecls
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for parsing comments with the ElvaParser
 */
class ElvaParserCommentTests {

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

}