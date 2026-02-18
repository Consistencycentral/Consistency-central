package com.habitarc.app.ui.whats_new

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitarc.app.ui.Divider
import com.habitarc.app.ui.HStack
import com.habitarc.app.ui.H_PADDING
import com.habitarc.app.ui.H_PADDING_HALF
import com.habitarc.app.ui.Screen
import com.habitarc.app.ui.VStack
import com.habitarc.app.ui.c
import com.habitarc.app.ui.halfDpFloor
import com.habitarc.app.ui.header.Header
import com.habitarc.app.ui.header.HeaderCancelButton
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.navigation.LocalNavigationLayer
import com.habitarc.app.ui.readme.ReadmeFs
import com.habitarc.app.ui.rememberVm
import com.habitarc.app.ui.squircleShape
import habitarc.shared.vm.readme.ReadmeVm
import habitarc.shared.vm.whats_new.WhatsNewVm

@Composable
fun WhatsNewFs() {

    val navigationFs = LocalNavigationFs.current
    val navigationLayer = LocalNavigationLayer.current

    val (_, state) = rememberVm {
        WhatsNewVm()
    }

    Screen {

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
            ),
        )

        LazyColumn(
            state = scrollState,
            contentPadding = PaddingValues(bottom = (H_PADDING * 2)),
        ) {

            item {

                state.historyItemsUi.forEach { historyItemUi ->

                    VStack(
                        modifier = Modifier
                            .padding(top = 12.dp),
                    ) {

                        HStack(
                            modifier = Modifier
                                .padding(horizontal = H_PADDING),
                        ) {

                            Text(
                                text = historyItemUi.dateText,
                                modifier = Modifier
                                    .weight(1f),
                                color = c.secondaryText,
                                fontWeight = FontWeight.Light,
                            )

                            Text(
                                text = historyItemUi.timeAgoText,
                                color = c.secondaryText,
                                fontWeight = FontWeight.Light,
                            )
                        }

                        Text(
                            text = historyItemUi.title,
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .padding(horizontal = H_PADDING),
                            color = c.text,
                            fontWeight = FontWeight.SemiBold,
                        )

                        val text = historyItemUi.text
                        if (text != null) {
                            Text(
                                text = text,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .padding(horizontal = H_PADDING),
                                fontWeight = FontWeight.Light,
                                color = c.secondaryText,
                            )
                        }

                        val buttonUi = historyItemUi.buttonUi
                        if (buttonUi != null) {
                            Text(
                                text = buttonUi.text,
                                modifier = Modifier
                                    .padding(top = 2.dp)
                                    .padding(horizontal = H_PADDING_HALF)
                                    .clip(squircleShape)
                                    .clickable {
                                        when (buttonUi) {
                                            WhatsNewVm.HistoryItemUi.ButtonUi.pomodoro -> {
                                                navigationFs.push {
                                                    ReadmeFs(
                                                        defaultItem = ReadmeVm.DefaultItem.pomodoro,
                                                    )
                                                }
                                            }
                                        }
                                    }
                                    .padding(horizontal = H_PADDING_HALF - halfDpFloor),
                                color = c.blue,
                            )
                        }

                        if (state.historyItemsUi.last() != historyItemUi) {
                            Divider(
                                modifier = Modifier
                                    .padding(top = 12.dp)
                                    .padding(start = H_PADDING),
                            )
                        }
                    }
                }
            }
        }
    }
}
