package net.elva.lang.ast

data class TypedefDecl(
    val name: String,
    val typeParams: List<Pair<String, TypeExpr>>,
    val fields: List<RecordFieldDecl>
) : TopLevelDecl()