package net.elva.core

interface Surface<in P : Purpose, out M : Msg> {
    fun draw(): M
}