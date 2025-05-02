package net.elva.runtime

import net.elva.core.Effect
import net.elva.core.NoOp
import net.elva.core.Purpose
import net.elva.core.ElvaRecord

object RuntimeLoop {

    fun <R: ElvaRecord, P : Purpose<R, R>> run(programPurpose: P, args: Array<String>) {
        var programModel = programPurpose.initModel(*args)

        while (true) {
            val surface = programPurpose.surface()
            val surfaceMsg = surface.draw(programModel)

            val (newPurpose, requestedEffects) = programPurpose.update(programModel, surfaceMsg)
            programModel = newPurpose

            // Handle the requested effects (sequential serial and non-nested for now)
            requestedEffects.forEach { effect: Effect ->
                effect.run { effectMsg ->

                    // If special NoOp msg is received, just continue
                    if (!(effectMsg is NoOp)) {
                        val (nextPurpose, _) = programPurpose.update(programModel, effectMsg)
                        programModel = nextPurpose
                    }
                }
            }
        }
    }
}
