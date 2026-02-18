package habitarc.shared.vm.notes

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import habitarc.shared.db.NoteDb
import habitarc.shared.onEachExIn
import habitarc.shared.vm.Vm

class NoteVm(
    noteDb: NoteDb,
) : Vm<NoteVm.State>() {

    data class State(
        val noteDb: NoteDb,
    )

    override val state = MutableStateFlow(
        State(
            noteDb = noteDb,
        )
    )

    init {
        val scopeVm = scopeVm()
        NoteDb.selectAscFlow().onEachExIn(scopeVm) { notesDb ->
            val newNoteDb: NoteDb? =
                notesDb.firstOrNull { it.id == noteDb.id }
            if (newNoteDb != null)
                state.update { it.copy(noteDb = newNoteDb) }
        }
    }
}
