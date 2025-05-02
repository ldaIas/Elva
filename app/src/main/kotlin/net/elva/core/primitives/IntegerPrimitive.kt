package net.elva.core.primitives

data class IntegerPrimitive(val value: Int) : NumericPrimitive<Int> {
    override fun getInner(): Int = value

    override fun plus(other: NumericPrimitive<Int>) = IntegerPrimitive(value + other.getInner())
    override fun minus(other: NumericPrimitive<Int>) = IntegerPrimitive(value - other.getInner())
    override fun times(other: NumericPrimitive<Int>) = IntegerPrimitive(value * other.getInner())
    override fun div(other: NumericPrimitive<Int>) = IntegerPrimitive(value / other.getInner())
}
