package net.elva.std.iofx

import net.elva.core.Effect
import net.elva.core.Msg
import net.elva.core.NoOp

/**
 * Standard provided effect to log a debugging message in standard output
 */
data class DebugEffect(private val msg: String) : Effect {
    override fun run(callback: (Msg) -> Unit) {
        println("DEBUG $msg")
        callback(NoOp)
    }
}