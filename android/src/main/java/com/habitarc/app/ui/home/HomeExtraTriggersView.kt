package com.habitarc.app.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitarc.app.ui.HStack
import com.habitarc.app.ui.c
import com.habitarc.app.ui.squircleShape
import com.habitarc.app.ui.checklists.ChecklistScreen
import com.habitarc.app.ui.navigation.LocalNavigationFs
import habitarc.shared.vm.home.HomeVm
import habitarc.shared.performUi

@Composable
fun HomeExtraTriggersView(
    extraTriggers: HomeVm.ExtraTriggers,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {

    if (extraTriggers.checklistsDb.isEmpty() &&
        extraTriggers.shortcutsDb.isEmpty()
    ) {
        return
    }

    val navigationFs = LocalNavigationFs.current

    LazyRow(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {

        extraTriggers.shortcutsDb.forEach { shortcutDb ->
            item {
                ButtonView(
                    title = shortcutDb.name,
                    iconType = IconType.shortcut,
                    onClick = {
                        shortcutDb.performUi()
                    },
                )
            }
        }

        extraTriggers.checklistsDb.forEach { checklistDb ->
            item {
                ButtonView(
                    title = checklistDb.name,
                    iconType = IconType.checklist,
                    onClick = {
                        navigationFs.push {
                            ChecklistScreen(
                                checklistDb = checklistDb,
                                withNavigationPadding = true,
                            )
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun ButtonView(
    title: String,
    iconType: IconType,
    onClick: () -> Unit,
) {

    val color: Color = when (iconType) {
        IconType.checklist -> c.green
        IconType.shortcut -> c.red
    }

    HStack(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .height(26.dp)
            .clip(squircleShape)
            .background(color)
            .clickable {
                onClick()
            }
            .padding(start = 8.dp, end = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            fontWeight = FontWeight.W400,
            color = c.white,
        )
    }
}

private enum class IconType {
    checklist,
    shortcut,
}
