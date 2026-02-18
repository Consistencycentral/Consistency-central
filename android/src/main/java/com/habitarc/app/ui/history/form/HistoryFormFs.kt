package com.habitarc.app.ui.history.form

import android.widget.NumberPicker
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.habitarc.app.ui.H_PADDING_HALF
import com.habitarc.app.ui.c
import com.habitarc.app.ui.dpToPx
import com.habitarc.app.isSdkQPlus
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.SpacerW1
import com.habitarc.app.ui.footer.Footer
import com.habitarc.app.ui.footer.FooterPlainButton
import com.habitarc.app.ui.form.FormItemView
import com.habitarc.app.ui.form.button.FormButton
import com.habitarc.app.ui.form.padding.FormPaddingTop
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import com.habitarc.app.ui.navigation.picker.NavigationPickerItem
import habitarc.shared.db.Goal2Db
import habitarc.shared.db.IntervalDb
import habitarc.shared.vm.history.form.HistoryFormUtils
import habitarc.shared.vm.history.form.HistoryFormVm

@Composable
fun HistoryFormFs(
    initIntervalDb: IntervalDb,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (vm, state) = rememberVm {
        HistoryFormVm(
            initIntervalDb = initIntervalDb,
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
                    vm.save(
                        dialogsManager = navigationFs,
                        onSuccess = {
                            navigationLayer.close()
                        },
                    )
                },
            ),
            cancelButton = HeaderCancelButton(
                text = "Cancel",
                onClick = {
                    navigationLayer.close()
                },
            ),
        )

        LazyColumn(
            state = scrollState,
            modifier = Modifier
                .weight(1f),
        ) {

            item {

                FormPaddingTop()

                FormButton(
                    title = state.goalTitle,
                    isFirst = true,
                    isLast = false,
                    note = state.goalNote,
                    noteColor = c.secondaryText,
                    withArrow = true,
                    onClick = {
                        navigationFs.picker(
                            title = state.goalTitle,
                            items = buildGoalsPickerItems(
                                goalsUi = state.goalsUi,
                                selectedGoalDb = state.goalDb,
                            ),
                            onDone = { newGoal ->
                                vm.setGoal(newGoal.item)
                            },
                        )
                    },
                )

                val timerItemsUi = state.timerItemsUi
                val formTimeItemIdx: MutableState<Int> = remember {
                    mutableStateOf(timerItemsUi.indexOfFirst { it.time == state.time })
                }

                val formTimeItemIdxValue: Int = formTimeItemIdx.value
                LaunchedEffect(formTimeItemIdxValue) {
                    vm.setTime(timerItemsUi[formTimeItemIdxValue].time)
                }

                FormItemView(
                    isFirst = false,
                    isLast = true,
                    modifier = Modifier,
                    content = {
                        AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 80.dp),
                            factory = { context ->
                                NumberPicker(context).apply {
                                    setOnValueChangedListener { _, _, new ->
                                        formTimeItemIdx.value = new
                                    }
                                    displayedValues = timerItemsUi.map { it.title }.toTypedArray()
                                    if (isSdkQPlus())
                                        textSize = dpToPx(18f).toFloat()
                                    wrapSelectorWheel = false
                                    minValue = 0
                                    maxValue = timerItemsUi.size - 1
                                    value = formTimeItemIdx.value // Set last
                                }
                            }
                        )
                    },
                )
            }
        }

        val intervalDb: IntervalDb? = state.initIntervalDb
        if (intervalDb != null) {

            Footer(
                scrollState = scrollState,
                contentModifier = Modifier
                    .padding(horizontal = H_PADDING_HALF),
            ) {

                FooterPlainButton(
                    text = "Delete",
                    color = c.red,
                    onClick = {
                        vm.delete(
                            intervalDb = intervalDb,
                            dialogsManager = navigationFs,
                            onSuccess = {
                                navigationLayer.close()
                            },
                        )
                    },
                )

                SpacerW1()

                FooterPlainButton(
                    text = HistoryFormUtils.moveToTasksTitle,
                    color = c.orange,
                    onClick = {
                        vm.moveToTasks(
                            intervalDb = intervalDb,
                            dialogsManager = navigationFs,
                            onSuccess = {
                                navigationLayer.close()
                            },
                        )
                    },
                )
            }
        }
    }
}

private fun buildGoalsPickerItems(
    goalsUi: List<HistoryFormVm.GoalUi>,
    selectedGoalDb: Goal2Db?,
): List<NavigationPickerItem<Goal2Db>> = goalsUi.map { goalUi ->
    NavigationPickerItem(
        title = goalUi.title,
        isSelected = selectedGoalDb?.id == goalUi.goalDb.id,
        item = goalUi.goalDb,
    )
}
