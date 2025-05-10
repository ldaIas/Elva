package net.elva.lang.ast

import net.elva.core.ElvaRecord
import net.elva.core.Msg

data class SurfaceDecl<M: ElvaRecord, E: Msg>(
    val name: String,
    val typeParams: Pair<Pair<String, TypeExpr>, Pair<String, TypeExpr>>,
    val draw: RecordFieldValueDecl
) : TopLevelDecl()