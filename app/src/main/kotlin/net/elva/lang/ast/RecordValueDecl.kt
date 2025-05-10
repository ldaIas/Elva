package net.elva.lang.ast

data class RecordValueDecl(
    val name: String,
    val fields: List<RecordFieldValueDecl>
) : TopLevelDecl()

data class RecordFieldValueDecl(
    val name: String,
    val type: Expr
)