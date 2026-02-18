package com.habitarc.app.ui.daytime_picker

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.habitarc.app.ui.VStack
import com.habitarc.app.ui.ZStack
import com.habitarc.app.ui.c
import com.habitarc.app.ui.roundedShape
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.SpacerW1
import com.habitarc.app.ui.header.sheet.HeaderSheet
import com.habitarc.app.ui.header.sheet.HeaderSheetButton
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import habitarc.shared.DaytimeUi

@Composable
fun DaytimePickerSheet(
    title: String,
    doneText: String,
    daytimeUi: DaytimeUi,
    withRemove: Boolean,
    onDone: (DaytimeUi) -> Unit,
    onRemove: () -> Unit,
) {

    val navigationLayer = LocalNavigationLayer.current

    val selectedHour = remember { mutableIntStateOf(daytimeUi.hour) }
    val selectedMinute = remember { mutableIntStateOf(daytimeUi.minute) }

    VStack {

        ZStack(Modifier.weight(1f))

        Screen(
            modifier = Modifier
                .weight(1f),
            bgColor = c.fg,
        ) {

            HeaderSheet(
                title = title,
                doneButton = HeaderSheetButton(
                    text = doneText,
                    onClick = {
                        val newDaytimeUi = DaytimeUi(
                            hour = selectedHour.intValue,
                            minute = selectedMinute.intValue,
                        )
                        onDone(newDaytimeUi)
                        navigationLayer.close()
                    },
                ),
                cancelButton = HeaderSheetButton(
                    text = "Cancel",
                    onClick = {
                        navigationLayer.close()
                    },
                ),
            )

            SpacerW1()

            DaytimePickerView(
                hour = selectedHour.intValue,
                minute = selectedMinute.intValue,
                onHourChanged = { hour -> selectedHour.intValue = hour },
                onMinuteChanged = { minute -> selectedMinute.intValue = minute },
            )

            if (withRemove) {
                Text(
                    text = "Remove",
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .clip(roundedShape)
                        .clickable {
                            onRemove()
                            navigationLayer.close()
                        }
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    color = c.red,
                )
            }

            ZStack(Modifier.navigationBarsPadding())

            SpacerW1()
        }
    }
}
