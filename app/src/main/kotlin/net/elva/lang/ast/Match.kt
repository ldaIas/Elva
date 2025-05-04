package net.elva.lang.ast

data class ExprMatch(
    val target: Expr,
    val branches: List<MatchBranch>
) : Expr()

data class MatchBranch(
    val pattern: String, // only type variant names for now
    val body: Expr
)