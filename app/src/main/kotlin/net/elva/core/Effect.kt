package net.elva.core

interface Effect {
    fun run(callback: (Msg) -> Unit)
}

