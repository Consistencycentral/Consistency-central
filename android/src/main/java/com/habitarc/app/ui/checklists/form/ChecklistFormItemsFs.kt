package com.habitarc.app.ui.checklists.form

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
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import habitarc.shared.db.ChecklistDb
import habitarc.shared.db.ChecklistItemDb
import habitarc.shared.vm.checklists.form.ChecklistFormItemsVm

@Composable
fun ChecklistFormItemsFs(
    checklistDb: ChecklistDb,
    onDelete: () -> Unit,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (vm, state) = rememberVm {
        ChecklistFormItemsVm(checklistDb = checklistDb)
    }

    Screen {

        val scrollState = rememberLazyListState()

        Header(
            title = state.checklistName,
            scrollState = scrollState,
            actionButton = HeaderActionButton(
                text = "Done",
                isEnabled = true,
                onClick = {
                    val isDoneAllowed = vm.isDoneAllowed(
                        dialogsManager = navigationFs,
                    )
                    if (isDoneAllowed) {
                        navigationLayer.close()
                    }
                },
            ),
            cancelButton = HeaderCancelButton(
                text = "Close",
                onClick = {
                    navigationLayer.close()
                },
            ),
        )

        fun openChecklistFormItemFs(checklistItemDb: ChecklistItemDb) {
            navigationFs.push {
                ChecklistFormItemFs(
                    checklistDb = checklistDb,
                    checklistItemDb = checklistItemDb,
                )
            }
        }

        FormSortedList(
            items = state.checklistItemsUi,
            itemId = { it.checklistItemDb.id },
            itemTitle = { it.text },
            onItemClick = { checklistItemUi ->
                openChecklistFormItemFs(checklistItemUi.checklistItemDb)
            },
            onItemLongClick = { checklistItemUi ->
                openChecklistFormItemFs(checklistItemUi.checklistItemDb)
            },
            scrollState = scrollState,
            modifier = Modifier
                .weight(1f),
            onMove = { fromIdx, toIdx ->
                vm.moveAndroidLocal(fromIdx, toIdx)
            },
            onFinish = {
                vm.moveAndroidSync()
            },
            onItemDelete = { itemUi ->
                vm.deleteItemWithConfirmation(
                    itemDb = itemUi.checklistItemDb,
                    dialogsManager = navigationFs,
                )
            },
        )

        Footer(
            scrollState = scrollState,
            contentModifier = Modifier
                .padding(horizontal = H_PADDING_HALF),
        ) {
            FooterAddButton(
                text = state.newItemText,
                onClick = {
                    navigationFs.push {
                        ChecklistFormItemFs(
                            checklistDb = checklistDb,
                            checklistItemDb = null,
                        )
                    }
                },
            )
            SpacerW1()
            FooterPlainButton(
                text = "Settings",
                color = c.blue,
                onClick = {
                    navigationFs.push {
                        ChecklistFormFs(
                            checklistDb = checklistDb,
                            onSave = {},
                            onDelete = {
                                onDelete()
                                navigationLayer.close()
                            },
                        )
                    }
                },
            )
        }
    }
}
