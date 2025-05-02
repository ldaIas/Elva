package net.elva.core.primitives

interface NumericPrimitive<T: Number> : Primitive<T> {
    operator fun plus(other: NumericPrimitive<T>): NumericPrimitive<T>
    operator fun minus (other: NumericPrimitive<T>): NumericPrimitive<T>
    operator fun times (other: NumericPrimitive<T>): NumericPrimitive<T>
    operator fun div (other: NumericPrimitive<T>): NumericPrimitive<T>
}