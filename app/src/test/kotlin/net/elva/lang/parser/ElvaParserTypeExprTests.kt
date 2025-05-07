package net.elva.lang.parser

import net.elva.lang.ast.*
import net.elva.lang.parser.ElvaParserTestUtils.parseTypeExpr
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Tests for parsing type expressions in the ElvaParser
 */
class ElvaParserTypeExprTests {

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
}