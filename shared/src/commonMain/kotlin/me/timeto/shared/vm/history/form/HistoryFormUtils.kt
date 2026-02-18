package habitarc.shared.vm.history.form

import habitarc.shared.UnixTime
import habitarc.shared.db.IntervalDb
import habitarc.shared.launchExIo
import habitarc.shared.textFeatures
import habitarc.shared.DialogsManager
import habitarc.shared.UiException
import habitarc.shared.db.Goal2Db

object HistoryFormUtils {

    const val moveToTasksTitle = "Move to Tasks"

    fun deleteIntervalUi(
        intervalDb: IntervalDb,
        dialogsManager: DialogsManager,
        onSuccess: suspend () -> Unit,
    ) {
        val note: String = makeIntervalDbNote(intervalDb)
        dialogsManager.confirmation(
            message = "Are you sure you want to delete \"$note\"",
            buttonText = "Delete",
            onConfirm = {
                launchExIo {
                    try {
                        intervalDb.delete()
                        onSuccess()
                    } catch (e: UiException) {
                        dialogsManager.alert(e.uiMessage)
                    }
                }
            },
        )
    }

    fun moveToTasksUi(
        intervalDb: IntervalDb,
        dialogsManager: DialogsManager,
        onSuccess: suspend () -> Unit,
    ) {
        val note: String = makeIntervalDbNote(intervalDb)
        dialogsManager.confirmation(
            message = "Are you sure you want to move to tasks \"$note\"",
            buttonText = "Move",
            onConfirm = {
                launchExIo {
                    try {
                        intervalDb.moveToTasks()
                        onSuccess()
                    } catch (e: UiException) {
                        dialogsManager.alert(e.uiMessage)
                    }
                }
            },
        )
    }

    fun makeTimeNote(
        time: Int,
        withToday: Boolean,
    ): String {
        val today: Int = UnixTime().localDay
        val unixTime = UnixTime(time)
        return if (today == unixTime.localDay) {
            (if (withToday) "Today " else "") +
            unixTime.getStringByComponents(
                listOf(
                    UnixTime.StringComponent.hhmm24,
                )
            )
        } else {
            unixTime.getStringByComponents(
                listOf(
                    UnixTime.StringComponent.dayOfMonth,
                    UnixTime.StringComponent.space,
                    UnixTime.StringComponent.month3,
                    UnixTime.StringComponent.comma,
                    UnixTime.StringComponent.space,
                    UnixTime.StringComponent.dayOfWeek3,
                    UnixTime.StringComponent.space,
                    UnixTime.StringComponent.hhmm24,
                )
            )
        }
    }
}

private fun makeIntervalDbNote(intervalDb: IntervalDb): String {
    val goalDb: Goal2Db = intervalDb.selectGoalDbCached()
    val intervalNote: String? = intervalDb.note?.takeIf { it.isNotBlank() }
    return (intervalNote ?: goalDb.name)
        .textFeatures()
        .textNoFeatures
}
