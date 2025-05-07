package net.elva.lang.parser

import net.elva.lang.ast.MsgDecl
import net.elva.lang.ast.MsgVariant
import net.elva.lang.parser.ElvaParserTestUtils.parseDecls
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for the Elva parser for msg declarations.
 */
class ElvaParserMsgTests {

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
}