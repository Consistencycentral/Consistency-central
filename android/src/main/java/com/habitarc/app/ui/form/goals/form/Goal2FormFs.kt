package com.habitarc.app.ui.goals.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.habitarc.app.toColor
import com.habitarc.app.ui.HStack
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.ZStack
import com.habitarc.app.ui.c
import com.habitarc.app.ui.checklists.ChecklistsPickerFs
import com.habitarc.app.ui.color_picker.ColorPickerFs
import com.habitarc.app.ui.form.FormInput
import com.habitarc.app.ui.form.FormSwitch
import com.habitarc.app.ui.form.button.FormButton
import com.habitarc.app.ui.form.button.FormButtonArrowView
import com.habitarc.app.ui.form.button.FormButtonView
import com.habitarc.app.ui.form.padding.FormPaddingBottom
import com.habitarc.app.ui.form.padding.FormPaddingSectionSection
import com.habitarc.app.ui.form.padding.FormPaddingTop
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import com.habitarc.app.ui.navigation.picker.NavigationPickerItem
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.roundedShape
import com.habitarc.app.ui.shortcuts.ShortcutsPickerFs
import com.habitarc.app.ui.timer.TimerSheet
import habitarc.shared.db.Goal2Db
import habitarc.shared.vm.goals.form.Goal2FormVm

@Composable
fun Goal2FormFs(
    goalDb: Goal2Db?,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (vm, state) = rememberVm {
        Goal2FormVm(
            initGoalDb = goalDb,
        )
    }

    Screen(
        modifier = Modifier
            .imePadding(),
    ) {

        val scrollState = rememberLazyListState()

        Header(
            title = state.title,
            scrollState = scrollState,
            actionButton = HeaderActionButton(
                text = state.doneText,
                isEnabled = state.isDoneEnabled,
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
            modifier = Modifier
                .weight(1f),
            state = scrollState,
        ) {

            item {

                FormPaddingTop()

                FormInput(
                    initText = state.name,
                    placeholder = state.namePlaceholder,
                    onChange = { newName ->
                        vm.setName(newName)
                    },
                    isFirst = true,
                    isLast = false,
                    isAutoFocus = goalDb == null,
                    imeAction = ImeAction.Done,
                )

                FormButton(
                    title = state.secondsTitle,
                    isFirst = false,
                    isLast = true,
                    note = state.secondsNote,
                    withArrow = true,
                    onClick = {
                        navigationFs.push {
                            TimerSheet(
                                title = state.secondsTitle,
                                doneTitle = "Done",
                                initSeconds = state.seconds,
                                hints = state.initGoalDb?.buildTimerHints() ?: emptyList(),
                                onDone = { newSeconds ->
                                    vm.setSeconds(newSeconds = newSeconds)
                                },
                            )
                        }
                    },
                )

                FormPaddingSectionSection()

                FormButton(
                    title = "Checklists",
                    isFirst = true,
                    isLast = false,
                    note = state.checklistsNote,
                    withArrow = true,
                    onClick = {
                        navigationFs.push {
                            ChecklistsPickerFs(
                                initChecklistsDb = state.checklistsDb,
                                onDone = { newChecklistsDb ->
                                    vm.setChecklistsDb(newChecklistsDb)
                                },
                            )
                        }
                    },
                )

                FormButton(
                    title = "Shortcuts",
                    isFirst = false,
                    isLast = true,
                    note = state.shortcutsNote,
                    withArrow = true,
                    onClick = {
                        navigationFs.push {
                            ShortcutsPickerFs(
                                initShortcutsDb = state.shortcutsDb,
                                onDone = { newShortcutsDb ->
                                    vm.setShortcutsDb(newShortcutsDb)
                                },
                            )
                        }
                    },
                )

                FormPaddingSectionSection()

                FormButton(
                    title = state.periodTitle,
                    isFirst = true,
                    isLast = true,
                    note = state.periodNote,
                    withArrow = true,
                    onClick = {
                        navigationFs.push {
                            GoalFormPeriodFs(
                                initGoalDbPeriod = state.period,
                                onDone = { newPeriod ->
                                    vm.setPeriod(newPeriod = newPeriod)
                                },
                            )
                        }
                    },
                )

                FormPaddingSectionSection()

                FormButton(
                    title = state.timerTypeTitle,
                    isFirst = true,
                    isLast = !state.showFixedTimerPicker,
                    note = state.timerTypeItemsUi.first { it.id == state.timerTypeId }.title,
                    withArrow = true,
                    onClick = {
                        navigationFs.picker(
                            title = state.timerTypeTitle,
                            items = buildTimerTypesPickerItems(
                                timerTypeItemsUi = state.timerTypeItemsUi,
                                selectedTimerTypeId = state.timerTypeId,
                            ),
                            onDone = { newTimerTypeUi ->
                                vm.setTimerTypeId(newTimerTypeUi.item.id)
                            },
                        )
                    },
                )

                if (state.showFixedTimerPicker) {
                    FormButton(
                        title = state.fixedTimerTitle,
                        isFirst = false,
                        isLast = true,
                        note = state.fixedTimerNote,
                        withArrow = true,
                        onClick = {
                            navigationFs.push {
                                TimerSheet(
                                    title = state.fixedTimerTitle,
                                    doneTitle = "Done",
                                    initSeconds = state.fixedTimer,
                                    hints = emptyList(),
                                    onDone = { seconds ->
                                        vm.setFixedTimer(newFixedTimer = seconds)
                                    },
                                )
                            }
                        },
                    )
                }

                FormPaddingSectionSection()

                FormButton(
                    title = state.parentGoalTitle,
                    isFirst = true,
                    isLast = true,
                    note = state.parentGoalUi?.title ?: "None",
                    withArrow = true,
                    onClick = {
                        navigationFs.picker(
                            title = state.parentGoalTitle,
                            items = buildGoalsPickerItems(
                                goalsUi = state.parentGoalsUi,
                                selectedGoalUi = state.parentGoalUi,
                            ),
                            onDone = { newGoal ->
                                vm.setParentGoalUi(newGoal.item)
                            },
                        )
                    },
                )

                FormPaddingSectionSection()

                FormButtonView(
                    title = state.colorTitle,
                    titleColor = null,
                    isFirst = true,
                    isLast = true,
                    modifier = Modifier,
                    rightView = {
                        HStack(
                            verticalAlignment = CenterVertically,
                        ) {
                            ZStack(
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(28.dp)
                                    .clip(roundedShape)
                                    .background(state.colorRgba.toColor()),
                            )
                            FormButtonArrowView()
                        }
                    },
                    onClick = {
                        navigationFs.push {
                            ColorPickerFs(
                                title = state.colorPickerTitle,
                                examplesUi = state.buildColorPickerExamplesUi(),
                                onDone = { newColorRgba ->
                                    vm.setColorRgba(newColorRgba = newColorRgba)
                                },
                            )
                        }
                    },
                    onLongClick = null,
                )

                FormPaddingSectionSection()

                FormButton(
                    title = state.pomodoroTitle,
                    isFirst = true,
                    isLast = false,
                    note = state.pomodoroNote,
                    withArrow = true,
                    onClick = {
                        navigationFs.picker(
                            title = state.pomodoroTitle,
                            items = buildPomodoroPickerItems(
                                pomodoroItemsUi = state.pomodoroItemsUi,
                                selectedPomodoroTimer = state.pomodoroTimer,
                            ),
                            onDone = { newPomodoroItemUi ->
                                vm.setPomodoroTimer(newPomodoroItemUi.item.timer)
                            },
                        )
                    },
                )

                FormButton(
                    title = "Timer Hints",
                    isFirst = false,
                    isLast = false,
                    note = state.timerHintsNote,
                    withArrow = true,
                    onClick = {
                        navigationFs.push {
                            GoalFormTimerHintsFs(
                                initTimerHints = state.timerHints,
                                onDone = { newTimerHints ->
                                    vm.setTimerHints(newTimerHints)
                                },
                            )
                        }
                    },
                )

                FormSwitch(
                    title = state.keepScreenOnTitle,
                    isEnabled = state.keepScreenOn,
                    isFirst = false,
                    isLast = true,
                    onChange = { newKeepScreenOn ->
                        vm.setKeepScreenOn(newKeepScreenOn = newKeepScreenOn)
                    },
                )

                if (goalDb != null) {
                    FormPaddingSectionSection()
                    FormButton(
                        title = "Delete Goal",
                        titleColor = c.red,
                        isFirst = true,
                        isLast = true,
                        onClick = {
                            vm.delete(
                                goalDb = goalDb,
                                dialogsManager = navigationFs,
                                onSuccess = {
                                    navigationLayer.close()
                                },
                            )
                        },
                    )
                }

                FormPaddingBottom(
                    withNavigation = true,
                )
            }
        }
    }
}

private fun buildGoalsPickerItems(
    goalsUi: List<Goal2FormVm.GoalUi>,
    selectedGoalUi: Goal2FormVm.GoalUi?,
): List<NavigationPickerItem<Goal2FormVm.GoalUi?>> {
    val list = mutableListOf<NavigationPickerItem<Goal2FormVm.GoalUi?>>()
    list.add(
        NavigationPickerItem(
            title = "None",
            isSelected = selectedGoalUi == null,
            item = null,
        )
    )
    goalsUi.forEach { goalUi ->
        list.add(
            NavigationPickerItem(
                title = goalUi.title,
                isSelected = selectedGoalUi?.goalDb?.id == goalUi.goalDb.id,
                item = goalUi,
            )
        )
    }
    return list
}

private fun buildTimerTypesPickerItems(
    timerTypeItemsUi: List<Goal2FormVm.TimerTypeItemUi>,
    selectedTimerTypeId: Goal2FormVm.TimerTypeItemUi.TimerTypeUiId,
): List<NavigationPickerItem<Goal2FormVm.TimerTypeItemUi>> {
    return timerTypeItemsUi.map { timerTypeItemUi ->
        NavigationPickerItem(
            title = timerTypeItemUi.title,
            isSelected = selectedTimerTypeId == timerTypeItemUi.id,
            item = timerTypeItemUi,
        )
    }
}

private fun buildPomodoroPickerItems(
    pomodoroItemsUi: List<Goal2FormVm.PomodoroItemUi>,
    selectedPomodoroTimer: Int,
): List<NavigationPickerItem<Goal2FormVm.PomodoroItemUi>> {
    return pomodoroItemsUi.map { pomodoroItemUi ->
        NavigationPickerItem(
            title = pomodoroItemUi.title,
            isSelected = selectedPomodoroTimer == pomodoroItemUi.timer,
            item = pomodoroItemUi,
        )
    }
}
