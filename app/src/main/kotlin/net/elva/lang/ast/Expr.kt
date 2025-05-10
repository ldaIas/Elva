package net.elva.lang.ast

sealed class Expr

data class ExprVar(val name: String) : Expr()
data object ExprUnit : Expr()
data class ExprParens(val inner: Expr) : Expr()

/* Primitive expressions */
data class ExprInt(val value: Int) : Expr()
data class ExprFloat(val value: Double) : Expr()
data class ExprString(val value: String) : Expr()
data class ExprBool(val value: Boolean) : Expr()


/**
 * Shorthanded valued record constructor
 * Example:
 * MyRecord "abc" 123 5.0 true
 */
data class ExprRecordCtor(
    val name: String,
    val args: List<Expr>
) : Expr()

/**
 * Named valued record constructor
 * Example:
 * MyRecord {
 *   name = "abc"
 *   age = 123
 *   weight = 5.0
 *   isAlive = true
 * }
 */
data class ExprRecordCtorNamed(
    val name: String,
    val args: List<Pair<String, Expr>>
) : Expr()