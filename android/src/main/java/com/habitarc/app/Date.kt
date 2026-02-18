package com.habitarc.app

import habitarc.shared.UnixTime
import java.util.Date

fun Date.toUnixTime() =
    UnixTime((this.time / 1_000L).toInt())
