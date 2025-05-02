package net.elva.core.records

import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import net.elva.core.primitives.PrimitiveFactory
import net.elva.core.primitives.Primitive
import net.elva.core.ElvaRecord

object ElvaRecordInitializer {
    fun <T: ElvaRecord> init(initType: KClass<T>): T {
        val constructor = initType.constructors.first()

        val args = constructor.parameters.associateWith { param -> ElvaRecordParamInitiliazer.init(param.type.classifier as KClass<*>)  }
        return constructor.callBy(args)
    }
}

object ElvaRecordParamInitiliazer {
    fun init(paramType: KClass<*>): Any? {
        return when {
            paramType.isSubclassOf(Primitive::class) -> PrimitiveFactory.getDefaultPrimitiveOf(paramType as KClass<Primitive<*>>)
            paramType.isSubclassOf(ElvaRecord::class) -> ElvaRecordInitializer.init(paramType as KClass<ElvaRecord>)
            else -> throw IllegalArgumentException("Unsupported parameter type in ElvaRecord: $paramType")
        }
    }

}