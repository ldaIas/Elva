package net.elva.runtime

import net.elva.core.Effect
import net.elva.core.NoOp
import net.elva.core.Program
import net.elva.core.Purpose

object RuntimeLoop {

    fun <P : Purpose> run(program: Program<P>) {
        var purpose = program.init()

        while (true) {
            val surface = purpose.surface()
            val surfaceMsg = surface.draw()

            val (newPurpose, requestedEffects) = program.update(purpose, surfaceMsg)
            purpose = newPurpose

            // Handle the requested effects (sequential serial and non-nested for now)
            requestedEffects.forEach { effect: Effect ->
                effect.run { effectMsg ->

                    // If special NoOp msg is received, just continue
                    if (!(effectMsg is NoOp)) {
                        val (nextPurpose, _) = program.update(purpose, effectMsg)
                        purpose = nextPurpose
                    }
                }
            }
        }
    }
}
