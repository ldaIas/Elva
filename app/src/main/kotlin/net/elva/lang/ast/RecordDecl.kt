package net.elva.lang.ast

data class RecordDecl(
    val name: String,
    val fields: List<RecordField>
) : TopLevelDecl()

data class RecordField(
    val name: String,
    val type: String
)