package net.elva.core.primitives

data class BooleanPrimitive(val value: Boolean) : Primitive<Boolean> {
    override fun getInner(): Boolean = value
}