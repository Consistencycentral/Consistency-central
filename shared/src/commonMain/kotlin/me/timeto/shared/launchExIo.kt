package habitarc.shared

import kotlinx.coroutines.CoroutineScope

fun launchExIo(block: suspend CoroutineScope.() -> Unit) =
    ioScope().launchEx(block)
