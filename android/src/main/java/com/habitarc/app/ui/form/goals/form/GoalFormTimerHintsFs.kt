package com.habitarc.app.ui.goals.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.habitarc.app.ui.H_PADDING
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.ZStack
import com.habitarc.app.ui.c
import com.habitarc.app.ui.form.plain.FormPlainButtonDeletion
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.home.HomeScreen__itemHeight
import com.habitarc.app.ui.home.HomeScreen__primaryFontSize
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.roundedShape
import com.habitarc.app.ui.timer.TimerSheet
import habitarc.shared.vm.goals.form.GoalFormTimerHintsVm

@Composable
fun GoalFormTimerHintsFs(
    initTimerHints: List<Int>,
    onDone: (List<Int>) -> Unit,
) {
    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (vm, state) = rememberVm {
        GoalFormTimerHintsVm(initTimerHints = initTimerHints)
    }

    Screen {

        Header(
            title = "Timer Hints",
            scrollState = null,
            actionButton = HeaderActionButton(
                text = "Done",
                isEnabled = true,
                onClick = {
                    onDone(vm.getTimerHints())
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

        state.timerHintsUi.forEach { timerHintUi ->
            FormPlainButtonDeletion(
                title = timerHintUi.text,
                isFirst = state.timerHintsUi.first() == timerHintUi,
                modifier = Modifier,
                onClick = {},
                onDelete = {
                    vm.delete(timerHintUi.seconds)
                },
            )
        }

        ZStack(
            modifier = Modifier
                .height(HomeScreen__itemHeight),
            contentAlignment = Alignment.CenterStart,
        ) {
            Text(
                text = "New Timer Hint",
                color = c.blue,
                modifier = Modifier
                    .clip(roundedShape)
                    .clickable {
                        navigationFs.push {
                            TimerSheet(
                                title = "Timer",
                                doneTitle = "Done",
                                initSeconds = 45 * 60,
                                hints = emptyList(),
                                onDone = { newTimer ->
                                    vm.add(seconds = newTimer)
                                },
                            )
                        }
                    }
                    .padding(vertical = 4.dp)
                    .padding(horizontal = H_PADDING),
                fontSize = HomeScreen__primaryFontSize,
            )
        }
    }
}
