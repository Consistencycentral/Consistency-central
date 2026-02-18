package com.habitarc.app.ui.history

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitarc.app.ui.HStack
import com.habitarc.app.ui.H_PADDING
import com.habitarc.app.ui.H_PADDING_HALF
import com.habitarc.app.MainActivity
import com.habitarc.app.ui.ZStack
import com.habitarc.app.ui.c
import com.habitarc.app.limitMin
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.roundedShape
import com.habitarc.app.ui.squircleShape
import com.habitarc.app.toColor
import com.habitarc.app.ui.history.form.HistoryFormFs
import com.habitarc.app.ui.navigation.LocalNavigationFs
import habitarc.shared.vm.history.HistoryVm

private const val barPxSecondsRatio: Int = 60

@Composable
fun HistoryFs() {

    val mainActivity = LocalActivity.current as MainActivity
    val navigationFs = LocalNavigationFs.current

    val (vm, state) = rememberVm {
        HistoryVm()
    }

    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(),
        state = scrollState,
        reverseLayout = true,
        contentPadding = PaddingValues(
            top = mainActivity.statusBarHeightDp,
            bottom = 56.dp,
        ),
    ) {

        state.daysUi.reversed().forEach { dayUi ->

            dayUi.intervalsUi.reversed().forEach { intervalUi ->

                item(key = "interval_${dayUi.unixDay}_${intervalUi.intervalDb.id}") {

                    val timeHeight = (intervalUi.secondsForBar / barPxSecondsRatio).dp

                    HStack(
                        modifier = Modifier
                            .padding(horizontal = H_PADDING_HALF)
                            .clip(squircleShape)
                            .clickable {
                                navigationFs.push {
                                    HistoryFormFs(
                                        initIntervalDb = intervalUi.intervalDb,
                                    )
                                }
                            },
                    ) {

                        Text(
                            text = intervalUi.timeString,
                            modifier = Modifier
                                .padding(vertical = 2.dp)
                                .padding(start = H_PADDING_HALF, end = H_PADDING),
                            color = if (intervalUi.isStartsPrevDay) c.transparent else c.text,
                            fontWeight = FontWeight.Medium,
                        )

                        ZStack(
                            modifier = Modifier
                                .padding(vertical = 4.dp)
                                .width(10.dp)
                                .height(10.dp.limitMin(timeHeight))
                                .align(Alignment.CenterVertically)
                                .clip(roundedShape)
                                .background(intervalUi.color.toColor())
                        )

                        Text(
                            text = intervalUi.text,
                            modifier = Modifier
                                .padding(start = H_PADDING)
                                .weight(1f)
                                .padding(vertical = 2.dp),
                            color = c.white,
                            fontWeight = FontWeight.Medium,
                        )

                        Text(
                            text = intervalUi.periodString,
                            modifier = Modifier
                                .padding(horizontal = H_PADDING)
                                .padding(vertical = 2.dp),
                            color = c.secondaryText,
                            fontWeight = FontWeight.Light,
                        )
                    }
                }
            }

            //
            // Header

            item(key = "day_${dayUi.unixDay}") {

                HStack(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.Center,
                ) {
                    Text(
                        text = dayUi.dayText,
                        modifier = Modifier
                            .clip(roundedShape)
                            .background(c.blue)
                            .padding(horizontal = 9.dp)
                            .padding(top = 2.dp, bottom = 1.dp),
                        color = c.white,
                        fontSize = 14.sp,
                    )

                    LaunchedEffect(Unit) {
                        if (state.daysUi.first() == dayUi) {
                            vm.loadNext {}
                        }
                    }
                }
            }
        }
    }
}
