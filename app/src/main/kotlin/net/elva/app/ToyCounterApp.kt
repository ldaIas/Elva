
package net.elva.app

import net.elva.core.*
import net.elva.core.primitives.IntegerPrimitive
import net.elva.core.primitives.PrimitiveFactory
import net.elva.core.records.ElvaRecordInitializer
import net.elva.std.iofx.DebugEffect
import net.elva.runtime.RuntimeLoop

object ToyCounterApp : Purpose<CounterModel, CounterModel> {

    override fun initModel(vararg args: String): CounterModel = ElvaRecordInitializer.init(CounterModel::class)

    override fun update(inModel: CounterModel, inMsg: Msg): Pair<CounterModel, List<Effect>> {
        return when (inMsg) {
            Increment -> Pair(CounterModel(inModel.count + PrimitiveFactory.getPrimitiveOf(1)), listOf(DebugEffect("Increment called. Current model val: $inModel")))
            Decrement -> Pair(CounterModel(inModel.count - PrimitiveFactory.getPrimitiveOf(1)), listOf(DebugEffect("Decrement called. Current model val: $inModel")))
            else -> Pair(inModel, listOf())
        }
    }

    override fun surface(): Surface<CounterModel, Msg> = CounterSurface<CounterModel>()
    
}

data class CounterModel(val count: IntegerPrimitive) : ElvaRecord

class CounterSurface<R: CounterModel> : Surface<R, CounterAppMsg> {
    override fun draw(inModel: R): CounterAppMsg {

        // Render
        val currentCount = inModel.count
        println("Counter Value: ${currentCount}")

        // Setup new Msgs
        println("Enter command: increment(i, +) or decrement(d, -)")
        val input = readln()
        val inputLower = input.lowercase()
        val msg = when (inputLower) {
            "i", "+" -> Increment
            "d", "-" -> Decrement
            else -> InvalidInput
        }
        return msg;
    }
}

sealed class CounterAppMsg : Msg
object Increment : CounterAppMsg()
object Decrement : CounterAppMsg()
object InvalidInput : CounterAppMsg()


fun main(args: Array<String>) {
    RuntimeLoop.run(ToyCounterApp, args)
}
