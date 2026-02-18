package com.habitarc.app.ui.tasks.folders

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import com.habitarc.app.ui.c
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.form.button.FormButton
import com.habitarc.app.ui.form.FormInput
import com.habitarc.app.ui.form.padding.FormPaddingTop
import com.habitarc.app.ui.form.padding.FormPaddingSectionSection
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import habitarc.shared.db.TaskFolderDb
import habitarc.shared.vm.tasks.folders.TaskFolderFormVm

@Composable
fun TaskFolderFormFs(
    initTaskFolderDb: TaskFolderDb?,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (vm, state) = rememberVm {
        TaskFolderFormVm(
            folderDb = initTaskFolderDb,
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
                    isLast = true,
                    isAutoFocus = true,
                    imeAction = ImeAction.Done,
                )

                val taskFolderDb: TaskFolderDb? = state.folderDb
                if (taskFolderDb != null) {
                    FormPaddingSectionSection()
                    FormButton(
                        title = state.deleteText,
                        titleColor = c.red,
                        isFirst = true,
                        isLast = true,
                        onClick = {
                            vm.delete(
                                folderDb = taskFolderDb,
                                dialogsManager = navigationFs,
                                onDelete = {
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
