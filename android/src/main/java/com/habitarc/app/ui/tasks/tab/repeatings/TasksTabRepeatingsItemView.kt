package com.habitarc.app.ui.tasks.tab.repeatings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.habitarc.app.R
import com.habitarc.app.ui.Divider
import com.habitarc.app.ui.HStack
import com.habitarc.app.ui.H_PADDING_HALF
import com.habitarc.app.ui.TriggersIconsView
import com.habitarc.app.ui.VStack
import com.habitarc.app.ui.ZStack
import com.habitarc.app.ui.c
import com.habitarc.app.ui.navigation.LocalNavigationFs
import com.habitarc.app.ui.repeatings.form.RepeatingFormFs
import com.habitarc.app.ui.squircleShape
import habitarc.shared.vm.tasks.tab.repeatings.TasksTabRepeatingsVm

@Composable
fun TasksTabRepeatingsItemView(
    repeatingUi: TasksTabRepeatingsVm.RepeatingUi,
    withTopDivider: Boolean,
    modifier: Modifier,
) {
    val navigationFs = LocalNavigationFs.current

    ZStack(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter,
    ) {

        VStack(
            modifier = Modifier
                .fillMaxWidth()
                .clip(squircleShape)
                .clickable {
                    navigationFs.push {
                        RepeatingFormFs(
                            initRepeatingDb = repeatingUi.repeatingDb,
                        )
                    }
                }
                .padding(vertical = 10.dp)
                .padding(start = H_PADDING_HALF),
        ) {

            HStack(
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {

                Text(
                    text = repeatingUi.dayLeftString,
                    modifier = Modifier
                        .weight(1f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W300,
                    color = c.secondaryText,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Text(
                    text = repeatingUi.dayRightString,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.W300,
                    color = c.secondaryText,
                    maxLines = 1,
                )
            }

            HStack(
                verticalAlignment = Alignment.CenterVertically,
            ) {

                Text(
                    text = repeatingUi.listText,
                    modifier = Modifier
                        .padding(top = 2.dp)
                        .weight(1f),
                    color = c.text,
                )

                TriggersIconsView(
                    checklistsDb = repeatingUi.textFeatures.checklistsDb,
                    shortcutsDb = repeatingUi.textFeatures.shortcutsDb,
                )

                if (repeatingUi.repeatingDb.isImportant) {
                    Icon(
                        painter = painterResource(R.drawable.sf_flag_fill_medium_regular),
                        contentDescription = "Important",
                        tint = c.red,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(17.dp),
                    )
                }
            }
        }

        if (withTopDivider)
            Divider(Modifier.padding(start = H_PADDING_HALF))
    }
}
