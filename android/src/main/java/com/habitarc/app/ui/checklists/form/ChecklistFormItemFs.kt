package com.habitarc.app.ui.checklists.form

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.checklists.ChecklistsPickerFs
import com.habitarc.app.ui.form.FormInput
import com.habitarc.app.ui.form.button.FormButton
import com.habitarc.app.ui.form.padding.FormPaddingSectionSection
import com.habitarc.app.ui.form.padding.FormPaddingTop
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import com.habitarc.app.ui.shortcuts.ShortcutsPickerFs
import habitarc.shared.db.ChecklistDb
import habitarc.shared.db.ChecklistItemDb
import habitarc.shared.vm.checklists.form.ChecklistFormItemVm

@Composable
fun ChecklistFormItemFs(
    checklistDb: ChecklistDb,
    checklistItemDb: ChecklistItemDb?,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (vm, state) = rememberVm {
        ChecklistFormItemVm(
            checklistDb = checklistDb,
            checklistItemDb = checklistItemDb,
        )
    }

    Screen {

        val scrollState = rememberLazyListState()

        Header(
            title = state.title,
            scrollState = scrollState,
            actionButton = HeaderActionButton(
                text = state.saveButtonText,
                isEnabled = state.isSaveEnabled,
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
                .fillMaxSize(),
            state = scrollState,
            contentPadding = PaddingValues(bottom = 25.dp),
        ) {

            item {
                FormPaddingTop()
            }

            item {
                FormInput(
                    initText = state.text,
                    placeholder = "",
                    onChange = { newText ->
                        vm.setText(newText)
                    },
                    isFirst = true,
                    isLast = true,
                    isAutoFocus = true,
                    imeAction = ImeAction.Done,
                )
            }

            item {
                FormPaddingSectionSection()
            }

            item {
                FormButton(
                    title = "Checklists",
                    isFirst = true,
                    isLast = false,
                    note = state.checklistsNote,
                    withArrow = true,
                    onClick = {
                        navigationFs.push {
                            ChecklistsPickerFs(
                                initChecklistsDb = state.nestedChecklistsDb,
                                onDone = { newChecklistsDb ->
                                    vm.setNestedChecklists(newChecklistsDb)
                                },
                            )
                        }
                    },
                )
            }

            item {
                FormButton(
                    title = "Shortcuts",
                    isFirst = false,
                    isLast = true,
                    note = state.shortcutsNote,
                    withArrow = true,
                    onClick = {
                        navigationFs.push {
                            ShortcutsPickerFs(
                                initShortcutsDb = state.nestedShortcutsDb,
                                onDone = { newShortcutsDb ->
                                    vm.setNestedShortcuts(newShortcutsDb)
                                }
                            )
                        }
                    },
                )
            }
        }
    }
}
