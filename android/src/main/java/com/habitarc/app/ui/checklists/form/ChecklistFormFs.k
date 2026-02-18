package com.habitarc.app.ui.checklists.form

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.habitarc.app.ui.c
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.form.button.FormButton
import com.habitarc.app.ui.form.FormInput
import com.habitarc.app.ui.form.FormSwitch
import com.habitarc.app.ui.form.padding.FormPaddingTop
import com.habitarc.app.ui.form.padding.FormPaddingSectionSection
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import habitarc.shared.db.ChecklistDb
import habitarc.shared.vm.checklists.form.ChecklistFormVm

@Composable
fun ChecklistFormFs(
    checklistDb: ChecklistDb?,
    onSave: (ChecklistDb) -> Unit,
    onDelete: () -> Unit,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (vm, state) = rememberVm {
        ChecklistFormVm(
            checklistDb = checklistDb,
        )
    }

    Screen {

        val scrollState = rememberLazyListState()

        Header(
            title = state.title,
            scrollState = scrollState,
            actionButton = HeaderActionButton(
                text = state.doneText,
                isEnabled = state.isSaveEnabled,
                onClick = {
                    vm.save(
                        dialogsManager = navigationFs,
                        onSuccess = { newChecklistDb ->
                            onSave(newChecklistDb)
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
                    initText = state.name,
                    placeholder = state.namePlaceholder,
                    onChange = { newName ->
                        vm.setName(newName)
                    },
                    isFirst = true,
                    isLast = true,
                    isAutoFocus = true,
                    imeAction = ImeAction.Done,
                )
            }

            item {
                FormPaddingSectionSection()
                FormSwitch(
                    title = "Reset at Start of Day",
                    isEnabled = state.isResetOnDayStarts,
                    isFirst = true,
                    isLast = true,
                    modifier = Modifier,
                    onChange = { newValue ->
                        vm.setIsResetOnDayStarts(newValue)
                    },
                )
            }

            if (checklistDb != null) {
                item {
                    FormPaddingSectionSection()
                    FormButton(
                        title = state.deleteText,
                        titleColor = c.red,
                        isFirst = true,
                        isLast = true,
                        onClick = {
                            vm.delete(
                                checklistDb = checklistDb,
                                dialogsManager = navigationFs,
                                onDelete = {
                                    onDelete()
                                    navigationLayer.close()
                                },
                            )
                        },
                    )
                }
            }
        }
    }
}
