package com.habitarc.app.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.habitarc.app.R
import com.habitarc.app.ui.checklists.ChecklistScreen
import com.habitarc.app.ui.navigation.LocalNavigationFs
import habitarc.shared.db.ChecklistDb
import habitarc.shared.db.ShortcutDb
import habitarc.shared.performUi

private val iconShape = RoundedCornerShape(6.dp)

@Composable
fun TriggersIconsView(
    checklistsDb: List<ChecklistDb>,
    shortcutsDb: List<ShortcutDb>,
) {

    val navigationFs = LocalNavigationFs.current

    if (checklistsDb.isEmpty() && shortcutsDb.isEmpty())
        return

    HStack {
        shortcutsDb.forEach { shortcutDb ->
            IconView(IconType.shortcut) {
                shortcutDb.performUi()
            }
        }
        checklistsDb.forEach { checklistDb ->
            IconView(IconType.checklist) {
                navigationFs.push {
                    ChecklistScreen(
                        checklistDb = checklistDb,
                        withNavigationPadding = true,
                    )
                }
            }
        }
    }
}

@Composable
private fun IconView(
    iconType: IconType,
    onClick: () -> Unit,
) {
    @DrawableRes val icon: Int = when (iconType) {
        IconType.checklist -> R.drawable.sf_checkmark_circle_fill_medium_regular
        IconType.shortcut -> R.drawable.sf_arrow_up_forward_circle_fill_medium_regular
    }
    val color: Color = when (iconType) {
        IconType.checklist -> c.green
        IconType.shortcut -> c.red
    }
    Icon(
        painter = painterResource(id = icon),
        contentDescription = null,
        tint = color,
        modifier = Modifier
            .padding(start = 8.dp)
            .size(18.dp)
            .clip(iconShape)
            .clickable {
                onClick()
            },
    )
}

private enum class IconType {
    checklist,
    shortcut,
}
