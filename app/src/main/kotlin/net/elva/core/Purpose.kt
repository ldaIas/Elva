package net.elva.core

interface Purpose {
    fun surface(): Surface<Purpose, Msg>
}