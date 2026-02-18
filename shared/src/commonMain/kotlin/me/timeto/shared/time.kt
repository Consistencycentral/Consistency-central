package habitarc.shared

import kotlinx.datetime.Clock

fun time(): Int =
    Clock.System.now().epochSeconds.toInt()
