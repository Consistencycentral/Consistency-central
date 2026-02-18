package com.habitarc.app

import androidx.compose.ui.graphics.Color
import habitarc.shared.ColorRgba

fun ColorRgba.toColor() =
    Color(r, g, b, a)
