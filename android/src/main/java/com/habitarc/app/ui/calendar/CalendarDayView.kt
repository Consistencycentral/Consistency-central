package com.habitarc.app.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitarc.app.ui.HStack
import com.habitarc.app.ui.H_PADDING_HALF
import com.habitarc.app.ui.VStack
import com.habitarc.app.ui.c
import com.habitarc.app.ui.onePx
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.roundedShape
import com.habitarc.app.ui.Divider
import com.habitarc.app.ui.SpacerW1
import com.habitarc.app.ui.calendar.list.CalendarListItemView
import com.habitarc.app.ui.events.EventFormFs
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.tasks.tab.repeatings.TasksTabRepeatingsItemView
import habitarc.shared.vm.calendar.CalendarDayVm

@Composable
fun CalendarDayView(
    unixDay: Int,
) {

    val navigationFs = LocalNavigationFs.current

    val (_, state) = rememberVm(unixDay) {
        CalendarDayVm(
            unixDay = unixDay,
        )
    }

    VStack(
        modifier = Modifier
            // Ignore swipe to action overflow
            .clipToBounds()
            .background(c.fg),
    ) {

        Divider(color = c.blue)

        HStack(
            modifier = Modifier
                .padding(horizontal = H_PADDING_HALF)
                .padding(top = 12.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            Text(
                text = state.inNote,
                color = c.white,
                fontSize = 14.sp,
            )

            SpacerW1()

            Text(
                text = state.newEventText,
                modifier = Modifier
                    .clip(roundedShape)
                    .background(c.blue)
                    .clickable {
                        navigationFs.push {
                            EventFormFs(
                                initEventDb = null,
                                initText = null,
                                initTime = state.initTime,
                                onDone = {},
                            )
                        }
                    }
                    .padding(horizontal = 10.dp)
                    .padding(top = 2.dp + onePx, bottom = 2.dp),
                color = c.white,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            )
        }

        state.itemsUi.forEachIndexed { idx, itemUi ->
            val isFirst: Boolean = idx == 0
            when (itemUi) {
                is CalendarDayVm.ItemUi.EventUi -> {
                    key("event_${itemUi.calendarListEventUi.eventDb.id}") {
                        CalendarListItemView(
                            eventUi = itemUi.calendarListEventUi,
                            withTopDivider = !isFirst,
                            clip = RectangleShape,
                            modifier = Modifier,
                        )
                    }
                }

                is CalendarDayVm.ItemUi.RepeatingUi -> {
                    key("repeating_${itemUi.repeatingsListRepeatingUi.repeatingDb.id}") {
                        TasksTabRepeatingsItemView(
                            repeatingUi = itemUi.repeatingsListRepeatingUi,
                            withTopDivider = !isFirst,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = H_PADDING_HALF),
                        )
                    }
                }
            }
        }
    }
}
