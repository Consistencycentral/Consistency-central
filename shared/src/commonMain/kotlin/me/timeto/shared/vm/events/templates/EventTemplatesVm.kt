package habitarc.shared.vm.events.templates

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import habitarc.shared.Cache
import habitarc.shared.db.EventTemplateDb
import habitarc.shared.onEachExIn
import habitarc.shared.vm.Vm

class EventTemplatesVm : Vm<EventTemplatesVm.State>() {

    data class State(
        val templatesUi: List<EventTemplateUi>
    ) {
        val newTemplateText = "New Template"
    }

    override val state = MutableStateFlow(
        State(
            templatesUi = Cache.eventTemplatesDbSorted.toTemplatesUi(),
        )
    )

    init {
        val scope = scopeVm()
        EventTemplateDb.selectAscSortedFlow().onEachExIn(scope) { templatesDb ->
            state.update {
                it.copy(templatesUi = templatesDb.toTemplatesUi())
            }
        }
    }
}

private fun List<EventTemplateDb>.toTemplatesUi(
): List<EventTemplateUi> = this.reversed().map { eventTemplateDb ->
    EventTemplateUi(
        eventTemplateDb = eventTemplateDb,
    )
}
