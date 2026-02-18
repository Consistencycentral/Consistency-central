package com.habitarc.app.ui.tasks.folders

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.habitarc.app.ui.H_PADDING_HALF
import com.habitarc.app.ui.c
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.SpacerW1
import com.habitarc.app.ui.footer.Footer
import com.habitarc.app.ui.footer.FooterAddButton
import com.habitarc.app.ui.footer.FooterPlainButton
import com.habitarc.app.ui.form.sorted.FormSortedList
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import habitarc.shared.db.TaskFolderDb
import habitarc.shared.vm.tasks.folders.TaskFoldersFormVm

@Composable
fun TaskFoldersFormFs() {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (vm, state) = rememberVm {
        TaskFoldersFormVm()
    }

    Screen(
        modifier = Modifier
            .imePadding(),
    ) {

        val scrollState = rememberLazyListState()

        Header(
            title = state.title,
            scrollState = scrollState,
            actionButton = null,
            cancelButton = HeaderCancelButton(
                text = "Close",
                onClick = {
                    navigationLayer.close()
                },
            )
        )

        fun openTaskFolderFormFs(taskFolderDb: TaskFolderDb) {
            navigationFs.push {
                TaskFolderFormFs(
                    initTaskFolderDb = taskFolderDb,
                )
            }
        }

        FormSortedList(
            items = state.foldersDb,
            itemId = { it.id },
            itemTitle = { it.name },
            onItemClick = { taskFolderDb ->
                openTaskFolderFormFs(taskFolderDb)
            },
            onItemLongClick = { taskFolderDb ->
                openTaskFolderFormFs(taskFolderDb)
            },
            onItemDelete = null,
            scrollState = scrollState,
            modifier = Modifier
                .weight(1f),
            onMove = { fromIdx, toIdx ->
                vm.moveAndroidLocal(fromIdx, toIdx)
            },
            onFinish = {
                vm.moveAndroidSync()
            }
        )

        Footer(
            scrollState = scrollState,
            contentModifier = Modifier
                .padding(horizontal = H_PADDING_HALF)
        ) {

            FooterAddButton(
                text = "New Folder",
                onClick = {
                    navigationFs.push {
                        TaskFolderFormFs(
                            initTaskFolderDb = null,
                        )
                    }
                },
            )

            SpacerW1()

            val tmrwButtonUi = state.tmrwButtonUi
            if (tmrwButtonUi != null) {
                FooterPlainButton(
                    text = tmrwButtonUi.text,
                    color = c.blue,
                    onClick = {
                        tmrwButtonUi.add(
                            dialogsManager = navigationFs,
                        )
                    },
                )
            }
        }
    }
}
