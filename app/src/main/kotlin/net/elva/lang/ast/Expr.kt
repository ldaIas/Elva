package net.elva.lang.ast

import net.elva.lang.tokens.TokenPosition

sealed class Expr

data class ExprVar(val name: String): Expr()
data class ExprUnit(val position: TokenPosition): Expr()
data class ExprParens(val inner: Expr) : Expr()