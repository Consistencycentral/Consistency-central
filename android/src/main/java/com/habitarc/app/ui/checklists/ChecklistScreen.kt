package com.habitarc.app.ui.checklists

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.checklists.form.ChecklistFormItemsFs
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.header.HeaderSecondaryButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import habitarc.shared.db.ChecklistDb

@Composable
fun ChecklistScreen(
    checklistDb: ChecklistDb,
    withNavigationPadding: Boolean,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    Screen {

        val scrollState = rememberLazyListState()

        Header(
            title = checklistDb.name,
            scrollState = scrollState,
            actionButton = null,
            cancelButton = HeaderCancelButton(
                text = "Close",
                onClick = {
                    navigationLayer.close()
                },
            ),
            secondaryButtons = listOf(
                HeaderSecondaryButton(
                    text = "Edit",
                    onClick = {
                        navigationFs.push {
                            ChecklistFormItemsFs(
                                checklistDb = checklistDb,
                                onDelete = {
                                    navigationLayer.close()
                                },
                            )
                        }
                    },
                )
            ),
        )

        ChecklistView(
            checklistDb = checklistDb,
            modifier = Modifier,
            scrollState = scrollState,
            maxLines = Int.MAX_VALUE,
            withAddButton = true,
            topPadding = 5.dp,
            bottomPadding = 16.dp,
            withNavigationPadding = withNavigationPadding,
        )
    }
}
