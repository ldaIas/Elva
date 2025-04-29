package net.elva.std.surfaceMsg

import net.elva.core.Purpose
import net.elva.core.Msg
import net.elva.core.Surface
import net.elva.core.Renderer


data class SimpleShellOut(val outStr: String) : Renderer {
    override fun render() {
        // Render the string given from the program in stdout
        println(outStr)
    }
}