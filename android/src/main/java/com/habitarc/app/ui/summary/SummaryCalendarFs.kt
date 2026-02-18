package com.habitarc.app.ui.summary

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.habitarc.app.MainActivity
import com.habitarc.app.ui.Divider
import com.habitarc.app.ui.HStack
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.ZStack
import com.habitarc.app.ui.c
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.roundedShape
import habitarc.shared.UnixTime
import habitarc.shared.vm.summary.SummaryCalendarVm

@Composable
fun SummaryCalendarFs(
    selectedStartTime: UnixTime,
    selectedFinishTime: UnixTime,
    onSelected: (UnixTime, UnixTime) -> Unit,
) {
    val navigationLayer = LocalNavigationLayer.current
    val mainActivity = LocalActivity.current as MainActivity

    val (vm, state) = rememberVm {
        SummaryCalendarVm(
            selectedStartTime = selectedStartTime,
            selectedFinishTime = selectedFinishTime,
        )
    }

    Screen {

        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp),
            reverseLayout = true,
        ) {
            item {
                ZStack(Modifier.navigationBarsPadding())
            }

            state.weeksUi.reversed().forEach { weekUi ->
                item {
                    HStack(
                        modifier = Modifier
                            .height(68.dp),
                    ) {
                        weekUi.daysUi.forEach { dayUi ->
                            ZStack(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f),
                            ) {
                                if (dayUi != null) {
                                    val subtitle = dayUi.subtitle
                                    if (subtitle != null) {
                                        Text(
                                            text = subtitle,
                                            modifier = Modifier
                                                .zIndex(2f)
                                                .align(Alignment.Center)
                                                .offset(y = (-20).dp),
                                            color = c.red,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                        )
                                    }
                                    val isSelected: Boolean = dayUi.unixDay in state.selectedDays
                                    ZStack(
                                        modifier = Modifier
                                            .zIndex(1f)
                                            .align(Alignment.Center)
                                            .size(32.dp)
                                            .clip(roundedShape)
                                            .background(if (isSelected) c.blue else c.transparent)
                                            .clickable {
                                                vm.selectDate(
                                                    unixTime = dayUi.timeStart,
                                                    onSelectionComplete = { timeStart, timeFinish ->
                                                        onSelected(timeStart, timeFinish)
                                                        navigationLayer.close()
                                                    },
                                                )
                                            },
                                        contentAlignment = Alignment.Center,
                                    ) {
                                        Text(
                                            text = dayUi.title,
                                            color = c.text,
                                            fontSize = (if (isSelected) 14.sp else 16.sp),
                                            fontWeight = (if (isSelected) FontWeight.SemiBold else FontWeight.Normal),
                                        )
                                    }
                                } else {
                                }
                            }
                        }
                    }

                    Divider()
                }
            }

            item {
                ZStack(Modifier.height(mainActivity.statusBarHeightDp))
            }
        }
    }
}
