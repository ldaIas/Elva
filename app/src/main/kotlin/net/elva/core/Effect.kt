package net.elva.core

interface Effect {
    fun run(callback: (Msg) -> Unit)
}

/**
 * Standard provided effect to log a debugging message in standard output
 */
data class DebugEffect(private val msg: String) : Effect {
    override fun run(callback: (Msg) -> Unit) {
        println("DEBUG $msg")
        callback(NoOp)
    }
}