package net.elva.lang.ast

data class MsgDecl(
    val name: String,
    val variants: List<MsgVariant>
) : TopLevelDecl()

data class MsgVariant(
    val name: String
)