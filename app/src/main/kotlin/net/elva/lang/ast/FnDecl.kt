package net.elva.lang.ast

data class FnDecl(
    val name: String,
    val params: List<FnParam>,
    val returnType: FnReturnType,  
    val body: Expr
) : TopLevelDecl()

data class FnParam(
    val name: String,
    val type: TypeExpr
)

data class FnReturnType(
    val types: List<TypeExpr>
)