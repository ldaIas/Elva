package net.elva.std.surfaces

import net.elva.core.Purpose
import net.elva.core.Msg
import net.elva.core.Surface
import net.elva.core.Renderer
import net.elva.core.SurfaceEvent


data class SimpleShellIn(val outStr: Renderer, val inputHandler: (disStr: String) -> Msg) : SurfaceEvent {
    override fun handle(): Msg {
        // Render the string given from the program in stdout
        outStr.render()

        // Read the user input and pass it to the program
        return inputHandler(readLine() ?: "")
    }
}