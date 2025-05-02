package net.elva.core.primitives

data class StringPrimitive(val value: String) : Primitive<String> {
    override fun getInner(): String = value
}