package habitarc.shared.vm.tasks.tab

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import habitarc.shared.Cache
import habitarc.shared.db.TaskFolderDb
import habitarc.shared.onEachExIn
import habitarc.shared.vm.Vm

class TasksTabVm : Vm<TasksTabVm.State>() {

    data class State(
        val taskFoldersUi: List<TaskFolderUi>,
        val initFolder: TaskFolderDb,
    )

    override val state = MutableStateFlow(
        State(
            taskFoldersUi =
                Cache.taskFoldersDbSorted.map { TaskFolderUi(it) },
            initFolder = Cache.getTodayFolderDb(),
        )
    )

    init {
        val scopeVm = scopeVm()
        TaskFolderDb.selectAllSortedFlow().onEachExIn(scopeVm) { folders ->
            state.update {
                it.copy(taskFoldersUi = folders.map { TaskFolderUi(it) })
            }
        }
    }

    ///

    data class TaskFolderUi(
        val taskFolderDb: TaskFolderDb,
    ) {
        val tabText: String =
            taskFolderDb.name.toTabText()
    }
}

private fun String.toTabText(): String =
    this.uppercase().split("").joinToString("\n").trim()
