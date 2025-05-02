package net.elva.core.primitives

import kotlin.reflect.KClass

sealed interface Primitive<V> {
    fun getInner(): V
}

object PrimitiveFactory {
    fun <T : Primitive<*>> getDefaultPrimitiveOf(primType: KClass<T>): T {
        return when (primType) {
            IntegerPrimitive::class -> IntegerPrimitive(0) as T
            BooleanPrimitive::class -> BooleanPrimitive(false) as T
            else -> throw IllegalArgumentException("No default primitive for type $primType")
        }
    }

    fun <T : Primitive<*>> getPrimitiveOf(value: Any): T {
        return when (value) {
            is Int -> IntegerPrimitive(value as Int) as T
            is Boolean -> BooleanPrimitive(value as Boolean) as T
            else -> throw IllegalArgumentException("No primitive for type $value")
        }
    }
}
