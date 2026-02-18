package com.habitarc.app.ui.calendar.list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.habitarc.app.ui.H_PADDING_HALF
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.squircleShape
import com.habitarc.app.ui.tasks.tab.TasksTabView__LIST_SECTION_PADDING
import com.habitarc.app.ui.tasks.tab.TasksTabView__PADDING_END
import habitarc.shared.vm.calendar.CalendarListVm

@Composable
fun CalendarListView(
    modifier: Modifier,
) {

    val (_, state) = rememberVm {
        CalendarListVm()
    }

    LazyColumn(
        modifier = modifier,
        reverseLayout = true,
        contentPadding = PaddingValues(
            end = TasksTabView__PADDING_END - H_PADDING_HALF,
            top = TasksTabView__LIST_SECTION_PADDING,
        ),
    ) {

        state.eventsUi.forEachIndexed { idx, eventUi ->
            item(key = eventUi.eventDb.id) {
                CalendarListItemView(
                    eventUi = eventUi,
                    withTopDivider = (state.eventsUi.size - 1) != idx,
                    clip = squircleShape,
                    modifier = Modifier
                        .padding(start = H_PADDING_HALF),
                )
            }
        }
    }
}
