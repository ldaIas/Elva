package net.elva.lang.ast

import net.elva.core.ElvaRecord
import net.elva.core.Msg

/**
 * Declaration class for purposes
 * For example:
 * purpose CounterAppPurpose <: V: CounterModel, E: CounterMsg =
 *     { model = \_ -> CounterModel
 *     , surface = \_ -> CounterSurface
 *     , update = update
 *     }
 */
data class PurposeDecl<M: ElvaRecord, E: Msg>(
    val name: String,
    val typeParams: Pair<Pair<String, TypeExpr>, Pair<String, TypeExpr>>,
    val initModel: RecordFieldValueDecl,
    val surfaceFn: RecordFieldValueDecl,
    val update: RecordFieldValueDecl
) : TopLevelDecl()