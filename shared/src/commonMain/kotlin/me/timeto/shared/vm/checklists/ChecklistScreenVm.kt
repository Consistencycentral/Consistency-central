package habitarc.shared.vm.checklists

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import habitarc.shared.db.ChecklistDb
import habitarc.shared.onEachExIn
import habitarc.shared.vm.Vm

class ChecklistScreenVm(
    checklistDb: ChecklistDb,
) : Vm<ChecklistScreenVm.State>() {

    data class State(
        val checklistDb: ChecklistDb,
    )

    override val state = MutableStateFlow(
        State(
            checklistDb = checklistDb,
        )
    )

    init {
        val scopeVm = scopeVm()
        ChecklistDb.selectAscFlow().onEachExIn(scopeVm) { checklistsDb ->
            val newChecklistDb: ChecklistDb? =
                checklistsDb.firstOrNull { it.id == checklistDb.id }
            if (newChecklistDb != null)
                state.update { it.copy(checklistDb = newChecklistDb) }
        }
    }
}
