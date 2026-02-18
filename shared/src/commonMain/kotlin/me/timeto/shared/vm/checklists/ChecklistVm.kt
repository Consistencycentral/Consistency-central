package habitarc.shared.vm.checklists

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import habitarc.shared.db.ChecklistDb
import habitarc.shared.db.ChecklistItemDb
import habitarc.shared.launchExIo
import habitarc.shared.onEachExIn
import habitarc.shared.DialogsManager
import habitarc.shared.TextFeatures
import habitarc.shared.moveUiListIos
import habitarc.shared.textFeatures
import habitarc.shared.vm.Vm

class ChecklistVm(
    checklistDb: ChecklistDb,
) : Vm<ChecklistVm.State>() {

    data class State(
        val checklistDb: ChecklistDb,
        val itemsDb: List<ChecklistItemDb>,
    ) {

        val stateUi: ChecklistStateUi =
            ChecklistStateUi.build(checklistDb, itemsDb)

        val itemsUi: List<ItemUi> =
            itemsDb.map { ItemUi(itemDb = it) }
    }

    override val state = MutableStateFlow(
        State(
            checklistDb = checklistDb,
            itemsDb = checklistDb.getItemsCached(),
        )
    )

    init {
        val scopeVm = scopeVm()
        ChecklistItemDb.selectSortedFlow().onEachExIn(scopeVm) { itemsDb ->
            state.update {
                it.copy(itemsDb = itemsDb.filter { it.list_id == checklistDb.id })
            }
        }
    }

    fun deleteItem(
        itemDb: ChecklistItemDb,
        dialogsManager: DialogsManager,
    ) {
        dialogsManager.confirmation(
            message = "Are you sure you want to delete \"${itemDb.text.textFeatures().textNoFeatures}\"?",
            buttonText = "Delete",
            onConfirm = {
                launchExIo {
                    itemDb.delete()
                }
            },
        )
    }

    fun moveIos(fromIdx: Int, toIdx: Int) {
        state.value.itemsDb.moveUiListIos(fromIdx, toIdx) {
            ChecklistItemDb.updateSortMany(itemsDb = it)
        }
    }

    ///

    class ItemUi(
        val itemDb: ChecklistItemDb,
    ) {

        val textFeatures: TextFeatures =
            itemDb.text.textFeatures()

        val text: String =
            textFeatures.textNoFeatures

        fun toggle() {
            launchExIo {
                itemDb.toggle()
            }
        }
    }
}
