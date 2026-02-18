package com.habitarc.app

import androidx.compose.ui.unit.Dp
import habitarc.shared.goldenRatio

fun Dp.limitMin(dp: Dp): Dp = if (this < dp) dp else this
fun Dp.limitMax(dp: Dp): Dp = if (this > dp) dp else this
fun Dp.goldenRatioUp(): Dp = this * goldenRatio
fun Dp.goldenRatioDown(): Dp = this / goldenRatio
