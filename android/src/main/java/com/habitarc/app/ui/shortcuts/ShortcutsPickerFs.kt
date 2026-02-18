package com.habitarc.app.ui.shortcuts

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.form.plain.FormPlainButtonSelection
import com.habitarc.app.ui.form.plain.FormPlainPaddingTop
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import habitarc.shared.db.ShortcutDb
import habitarc.shared.vm.shortcuts.ShortcutsPickerVm

// todo context menu
// todo new shortcut button

@Composable
fun ShortcutsPickerFs(
    initShortcutsDb: List<ShortcutDb>,
    onDone: (List<ShortcutDb>) -> Unit,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (vm, state) = rememberVm {
        ShortcutsPickerVm(
            initShortcutsDb = initShortcutsDb,
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
                    onDone(vm.getSelectedShortcutsDb())
                    navigationLayer.close()
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
                FormPlainPaddingTop()
            }

            state.shortcutsDbSorted.forEachIndexed { idx, shortcutDb ->
                item(key = shortcutDb.id) {
                    FormPlainButtonSelection(
                        title = shortcutDb.name,
                        isSelected = shortcutDb.id in state.selectedIds,
                        isFirst = idx == 0,
                        modifier = Modifier
                            .animateItem(),
                        onClick = {
                            vm.toggleShortcut(shortcutDb)
                        },
                    )
                }
            }
        }
    }
}
