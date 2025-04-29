package net.elva.core

interface Program<P : Purpose> { 
    fun init(): P
    fun update(purpose: P, msg: Msg): Pair<P, List<Effect>>
}