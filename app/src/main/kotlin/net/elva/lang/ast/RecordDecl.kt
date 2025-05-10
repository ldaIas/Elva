package net.elva.lang.ast

data class RecordDecl(
    val name: String,
    val fields: List<RecordFieldDecl>
) : TopLevelDecl()

data class RecordFieldDecl(
    val name: String,
    val type: TypeExpr
)