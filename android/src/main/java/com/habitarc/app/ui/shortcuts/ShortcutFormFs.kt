package com.habitarc.app.ui.shortcuts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.habitarc.app.ui.HStack
import com.habitarc.app.ui.H_PADDING
import com.habitarc.app.R
import com.habitarc.app.ui.c
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.roundedShape
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.form.button.FormButton
import com.habitarc.app.ui.form.FormHeader
import com.habitarc.app.ui.form.FormInput
import com.habitarc.app.ui.form.padding.FormPaddingTop
import com.habitarc.app.ui.form.padding.FormPaddingSectionHeader
import com.habitarc.app.ui.form.button.FormButtonView
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderActionButton
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import com.habitarc.app.ui.shortcuts.apps.ShortcutAppsFs
import habitarc.shared.db.ShortcutDb
import habitarc.shared.vm.shortcuts.ShortcutFormVm

@Composable
fun ShortcutFormFs(
    shortcutDb: ShortcutDb?,
) {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current
    val keyboardController = LocalSoftwareKeyboardController.current

    val (vm, state) = rememberVm {
        ShortcutFormVm(
            shortcutDb = shortcutDb,
        )
    }

    Screen(
        modifier = Modifier
            .imePadding(),
    ) {

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
                text = "Close",
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

                val triggerReinit = remember { mutableStateOf(false) }

                fun reinitForm(
                    name: String,
                    uri: String,
                ) {
                    vm.setName(name)
                    vm.setUri(uri)
                    triggerReinit.value = !triggerReinit.value
                    keyboardController?.hide()
                }

                //
                // Name

                FormPaddingTop()

                FormHeader(state.nameHeader)

                FormInput(
                    initText = state.name,
                    placeholder = state.namePlaceholder,
                    onChange = { newName ->
                        vm.setName(newName)
                    },
                    isFirst = true,
                    isLast = true,
                    isAutoFocus = false,
                    imeAction = ImeAction.Done,
                    triggerReinit = triggerReinit.value,
                )

                //
                // Uri

                FormPaddingSectionHeader()

                FormHeader(state.uriHeader)

                FormInput(
                    initText = state.uri,
                    placeholder = state.uriPlaceholder,
                    onChange = { newUri ->
                        vm.setUri(newUri)
                    },
                    isFirst = true,
                    isLast = true,
                    isAutoFocus = false,
                    imeAction = ImeAction.Done,
                    triggerReinit = triggerReinit.value,
                )

                //
                // Examples

                FormPaddingSectionHeader()

                FormHeader("EXAMPLES")

                shortcutExamples.forEach { example ->
                    FormButtonView(
                        title = example.name,
                        titleColor = c.text,
                        isFirst = shortcutExamples.first() == example,
                        isLast = shortcutExamples.last() == example,
                        modifier = Modifier,
                        rightView = {
                            HStack(
                                modifier = Modifier
                                    .padding(end = H_PADDING)
                            ) {
                                Text(
                                    text = example.hint,
                                    color = c.secondaryText,
                                )
                                AnimatedVisibility(
                                    visible = state.uri == example.uri,
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.sf_checkmark_medium_medium),
                                        contentDescription = "Selected",
                                        tint = c.green,
                                        modifier = Modifier
                                            .padding(start = 8.dp)
                                            .size(18.dp)
                                            .alpha(0.8f)
                                            .clip(roundedShape)
                                            .padding(3.dp)
                                    )
                                }
                            }
                        },
                        onClick = {
                            reinitForm(
                                name = example.name,
                                uri = example.uri,
                            )
                        },
                        onLongClick = {},
                    )
                }

                FormPaddingSectionHeader()

                FormButton(
                    title = "Apps",
                    isFirst = true,
                    isLast = true,
                    onClick = {
                        navigationFs.push {
                            ShortcutAppsFs(
                                onAppSelected = { shortcutApp ->
                                    reinitForm(
                                        name = shortcutApp.name,
                                        uri = vm.prepUriForAndroidPackage(
                                            androidPackage = shortcutApp.androidPackage
                                        ),
                                    )
                                }
                            )
                        }
                    },
                )

                if (shortcutDb != null) {
                    FormPaddingSectionHeader()
                    FormButton(
                        title = state.deleteText,
                        titleColor = c.red,
                        isFirst = true,
                        isLast = true,
                        onClick = {
                            vm.delete(
                                shortcutDb = shortcutDb,
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

//
// Examples Data

private class ShortcutExample(
    val name: String,
    val hint: String,
    val uri: String,
)

private val shortcutExamples = listOf(
    ShortcutExample(
        name = "10-Minute Meditation",
        hint = "Youtube",
        uri = "https://www.youtube.com/watch?v=O-6f5wQXSu8",
    ),
    ShortcutExample(
        name = "Play a Song 😈",
        hint = "Music App",
        uri = "https://music.youtube.com/watch?v=ikFFVfObwss&feature=share",
    ),
)
