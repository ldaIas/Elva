package net.elva.lang.parser;

import net.elva.lang.ast.ImportDecl
import net.elva.lang.ast.ImportItem
import kotlin.test.assertEquals
import org.junit.jupiter.api.Test
import net.elva.lang.parser.ElvaParserTestUtils.parseDecls

/**
 * Tests for parsing import statements with the ElvaParser
 */
class ElvaParserImportTests {

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

}
