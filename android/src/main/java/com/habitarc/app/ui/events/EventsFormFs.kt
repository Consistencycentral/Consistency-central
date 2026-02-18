package com.habitarc.app.ui.events

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import com.habitarc.app.ui.HStack
import com.habitarc.app.ui.H_PADDING
import com.habitarc.app.ui.VStack
import com.habitarc.app.ui.ZStack
import com.habitarc.app.ui.c
import com.habitarc.app.ui.halfDpCeil
import com.habitarc.app.ui.onePx
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.roundedShape
import com.habitarc.app.ui.squircleShape
import com.habitarc.app.ui.daytime_picker.DaytimePickerSliderView
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.SpacerW1
import com.habitarc.app.ui.events.templates.EventTemplatesView
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.header.HeaderSecondaryButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import com.habitarc.app.ui.showDatePicker
import habitarc.shared.UnixTime
import habitarc.shared.db.EventDb
import habitarc.shared.vm.events.EventFormVm

@Composable
fun EventFormFs(
    initEventDb: EventDb?,
    initText: String?,
    initTime: Int?,
    onDone: () -> Unit,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val focusRequester = remember { FocusRequester() }
    val isFocused = remember { mutableStateOf(true) }

    val (vm, state) = rememberVm {
        EventFormVm(
            initEventDb = initEventDb,
            initText = initText,
            initTime = initTime,
        )
    }

    val textField = remember {
        val stateText = state.text
        mutableStateOf(TextFieldValue(stateText, TextRange(stateText.length)))
    }

    Screen(
        modifier = Modifier
            .imePadding(),
    ) {

        val scrollState = rememberScrollState()

        Header(
            title = "Event",
            scrollState = scrollState,
            actionButton = null,
            cancelButton = HeaderCancelButton(
                text = "Cancel",
                onClick = {
                    navigationLayer.close()
                },
            ),
            secondaryButtons = if (initEventDb == null) emptyList() else listOf(
                HeaderSecondaryButton(
                    text = "Delete",
                    onClick = {
                        vm.delete(
                            eventDb = initEventDb,
                            dialogsManager = navigationFs,
                            onSuccess = {
                                navigationLayer.close()
                            },
                        )
                    },
                ),
            ),
        )

        VStack(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(
                    state = scrollState,
                    reverseScrolling = true,
                )
        ) {

            SpacerW1()

            DateTimeButton(
                text = state.selectedDateText,
                paddingStart = H_PADDING,
                onClick = {
                    navigationFs.showDatePicker(
                        unixTime = state.selectedUnixTime,
                        minTime = UnixTime(state.minTime),
                        maxTime = UnixTime(UnixTime.MAX_TIME),
                        onDone = { newUnixTime ->
                            vm.setUnixDay(newUnixTime.localDay)
                        },
                    )
                },
            )

            DaytimePickerSliderView(
                daytimeUi = state.daytimeUi,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 10.dp),
                onChange = { daytimeUi ->
                    vm.setDaytime(daytimeUi)
                },
            )

            EventTemplatesView(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                onDone = { templateUi ->
                    val newText = vm.setTemplate(templateUi.eventTemplateDb)
                    textField.value = TextFieldValue(newText, TextRange(newText.length))
                },
            )
        }

        HStack(
            modifier = Modifier
                .background(c.fg)
                .padding(vertical = 4.dp)
                .navigationBarsPadding()
                .imePadding(),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            ZStack(
                modifier = Modifier
                    .weight(1f),
            ) {

                val text: String = textField.value.text

                BasicTextField(
                    value = textField.value,
                    onValueChange = { newValue ->
                        textField.value = newValue
                        vm.setText(newValue.text)
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            isFocused.value = it.isFocused
                        },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        imeAction = ImeAction.Done,
                    ),
                    cursorBrush = SolidColor(c.blue),
                    textStyle = LocalTextStyle.current.copy(
                        color = c.text,
                        fontSize = 16.sp,
                    ),
                    decorationBox = { innerTextField ->
                        ZStack(
                            modifier = Modifier
                                .fillMaxWidth()
                                .sizeIn(minHeight = 40.dp)
                                .padding(
                                    start = H_PADDING,
                                    end = H_PADDING + 16.dp, // For clear button
                                    // Top and bottom for multiline padding
                                    top = 8.dp + halfDpCeil,
                                    bottom = 8.dp,
                                ),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = state.textPlaceholder,
                                    style = LocalTextStyle.current.copy(
                                        color = c.text.copy(alpha = 0.3f),
                                        fontSize = 16.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    },
                )
            }

            Text(
                text = state.doneText,
                modifier = Modifier
                    .padding(start = 10.dp, end = H_PADDING)
                    .clip(roundedShape)
                    .background(c.blue)
                    .clickable {
                        vm.save(
                            dialogsManager = navigationFs,
                            onSuccess = {
                                onDone()
                                navigationLayer.close()
                            },
                        )
                    }
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                color = c.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
            )
        }
    }

    LaunchedEffect(Unit) {
        delay(100) // Otherwise does not work for dialogs
        focusRequester.requestFocus()
    }
}

@Composable
private fun DateTimeButton(
    text: String,
    paddingStart: Dp,
    onClick: () -> Unit,
) {
    Text(
        text = text,
        modifier = Modifier
            .padding(start = paddingStart)
            .clip(squircleShape)
            .background(c.fg)
            .clickable {
                onClick()
            }
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .padding(top = onePx),
        color = c.text,
        fontSize = 15.sp,
        fontWeight = FontWeight.Medium,
    )
}
