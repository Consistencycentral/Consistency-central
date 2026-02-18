package habitarc.shared

fun zlog(message: Any?): Unit =
    println(";; ${message.toString().replace("\n", "\n;; ")}")
