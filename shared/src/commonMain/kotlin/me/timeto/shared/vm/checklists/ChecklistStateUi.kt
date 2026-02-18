package habitarc.shared.vm.checklists

import habitarc.shared.db.ChecklistDb
import habitarc.shared.db.ChecklistItemDb
import habitarc.shared.launchExIo

sealed class ChecklistStateUi(
    val actionDesc: String,
    val onClick: () -> Unit,
) {

    companion object {

        fun build(
            checklist: ChecklistDb,
            items: List<ChecklistItemDb>,
        ): ChecklistStateUi = when {
            items.all { it.isChecked } -> Completed(checklist)
            items.none { it.isChecked } -> Empty(checklist)
            else -> Partial(checklist)
        }
    }

    ///

    class Completed(checklist: ChecklistDb) : ChecklistStateUi("Uncheck All", {
        launchExIo {
            ChecklistItemDb.toggleByList(checklist, false)
        }
    })

    class Empty(checklist: ChecklistDb) : ChecklistStateUi("Check All", {
        launchExIo {
            ChecklistItemDb.toggleByList(checklist, true)
        }
    })

    class Partial(checklist: ChecklistDb) : ChecklistStateUi("Uncheck All", {
        launchExIo {
            ChecklistItemDb.toggleByList(checklist, false)
        }
    })
}
