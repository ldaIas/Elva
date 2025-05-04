package net.elva.lang.ast

/**
 * Compile time "expression" that boils down to a type that can be enforced on the units
 */
sealed class TypeExpr

data class TypeVar(val name: String) : TypeExpr() // like `A`, `E`
data class TypeNamed(val name: String) : TypeExpr() // like `String`, `CounterModel`
data class TypeFunc(val from: TypeExpr, val to: TypeExpr) : TypeExpr() // A |-> B
data class TypeApplied(val base: TypeExpr, val arg: TypeExpr) : TypeExpr() // List A, Dict K V
data class TypeTuple(val types: List<TypeExpr>) : TypeExpr() // (A, B, C)
data object TypeUnit : TypeExpr() // void / nil / unit / empty type