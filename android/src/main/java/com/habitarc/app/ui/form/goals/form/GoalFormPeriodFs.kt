package com.habitarc.app.ui.goals.form

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.form.padding.FormPaddingTop
import com.habitarc.app.ui.form.plain.FormPlainButtonSelection
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import habitarc.shared.db.Goal2Db
import habitarc.shared.vm.goals.form.GoalFormPeriodVm

@Composable
fun GoalFormPeriodFs(
    initGoalDbPeriod: Goal2Db.Period,
    onDone: (Goal2Db.Period) -> Unit,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (vm, state) = rememberVm {
        GoalFormPeriodVm(
            initGoalDbPeriod = initGoalDbPeriod,
        )
    }

    Screen {

        val scrollState = rememberLazyListState()

        Header(
            title = state.title,
            scrollState = scrollState,
            actionButton = HeaderActionButton(
                text = state.doneText,
                isEnabled = true,
                onClick = {
                    val period: Goal2Db.Period = state.buildPeriodOrNull(
                        dialogsManager = navigationFs,
                    ) ?: return@HeaderActionButton
                    onDone(period)
                    navigationLayer.close()
                },
            ),
            cancelButton = HeaderCancelButton(
                text = "Cancel",
                onClick = {
                    navigationLayer.close()
                },
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f),
            state = scrollState,
        ) {

            item {

                FormPaddingTop()

                vm.daysOfWeek.forEachIndexed { idx, dayOfWeek ->
                    FormPlainButtonSelection(
                        title = dayOfWeek.title,
                        isSelected = dayOfWeek.id in state.selectedDaysOfWeek,
                        isFirst = idx == 0,
                        modifier = Modifier,
                        onClick = {
                            vm.toggleDayOfWeek(dayOfWeek = dayOfWeek)
                        },
                    )
                }
            }
        }
    }
}
