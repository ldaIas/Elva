
package net.elva.app

import net.elva.core.*
import net.elva.runtime.RuntimeLoop

object ToyCounterApp : Program<CounterModel> {

    override fun init(): CounterModel {
        return CounterModel(0)
    }

    override fun update(purpose: CounterModel, msg: Msg): Pair<CounterModel, List<Effect>> {
        return when (msg) {
            Increment -> Pair(CounterModel(purpose.count + 1), listOf(DebugEffect("Increment called. Current model val: $purpose")))
            Decrement -> Pair(CounterModel(purpose.count - 1), listOf(DebugEffect("Decrement called. Current model val: $purpose")))
            else -> Pair(purpose, listOf())
        }
    }
}

data class CounterModel(val count: Int) : Purpose {
    override fun surface(): Surface<Purpose, Msg> {
        return CounterSurface(this)
    }
}

data class CounterSurface<P: Purpose>(val purpose: P) : Surface<P, CounterAppMsg> {
    override fun draw(): CounterAppMsg {

        // Render
        println("Counter Value: $purpose.count")

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


fun main() {
    RuntimeLoop.run(ToyCounterApp)
}
