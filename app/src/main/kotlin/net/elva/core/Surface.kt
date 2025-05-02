package net.elva.core

interface Surface<in R: ElvaRecord, out M : Msg> {
    fun draw(inModel: R): M
}