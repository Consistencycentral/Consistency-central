package com.habitarc.app.ui.settings

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.form.plain.FormPlainButtonSelection
import com.habitarc.app.ui.form.plain.FormPlainPaddingTop
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import habitarc.shared.vm.settings.SettingsVm

@Composable
fun SettingsDayStartFs(
    vm: SettingsVm,
    state: SettingsVm.State,
) {

    val navigationLayer = LocalNavigationLayer.current

    Screen(
        modifier = Modifier
            .navigationBarsPadding(),
    ) {

        val scrollState = rememberLazyListState()

        Header(
            title = "Day Start",
            scrollState = scrollState,
            actionButton = HeaderActionButton(
                text = "Done",
                isEnabled = true,
                onClick = {
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
                .fillMaxSize(),
            state = scrollState,
        ) {

            item {
                FormPlainPaddingTop()
            }

            val dayStartListItems = state.dayStartListItems
            dayStartListItems.forEach { item ->
                item {
                    FormPlainButtonSelection(
                        title = item.note,
                        isSelected = item.seconds == state.dayStartSeconds,
                        isFirst = dayStartListItems.first() == item,
                        modifier = Modifier,
                        onClick = {
                            vm.setDayStartOffsetSeconds(item.seconds)
                            navigationLayer.close()
                        },
                    )
                }
            }
        }
    }
}
