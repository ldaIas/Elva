package net.elva.core

interface SurfaceEvent {
    fun handle(): Msg
}