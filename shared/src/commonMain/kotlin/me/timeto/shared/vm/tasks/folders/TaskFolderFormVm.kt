package habitarc.shared.vm.tasks.folders

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import habitarc.shared.db.TaskDb
import habitarc.shared.db.TaskFolderDb
import habitarc.shared.launchExIo
import habitarc.shared.DialogsManager
import habitarc.shared.UiException
import habitarc.shared.vm.Vm

class TaskFolderFormVm(
    folderDb: TaskFolderDb?,
) : Vm<TaskFolderFormVm.State>() {

    data class State(
        val folderDb: TaskFolderDb?,
        val name: String,
    ) {

        val title: String =
            if (folderDb != null) "Edit Folder" else "New Folder"

        val doneText: String =
            if (folderDb != null) "Done" else "Create"

        val namePlaceholder = "Folder Name"
        val isSaveEnabled: Boolean = name.isNotBlank()
        val deleteText = "Delete Folder"
    }

    override val state = MutableStateFlow(
        State(
            folderDb = folderDb,
            name = folderDb?.name ?: "",
        )
    )

    fun setName(name: String) {
        state.update { it.copy(name = name) }
    }

    fun save(
        dialogsManager: DialogsManager,
        onSuccess: () -> Unit,
    ): Unit = launchExIo {
        try {
            val name: String = state.value.name
            val folderDb: TaskFolderDb? = state.value.folderDb
            if (folderDb != null)
                folderDb.updateNameWithValidation(name)
            else
                TaskFolderDb.insertWithValidation(name)
            onUi { onSuccess() }
        } catch (e: UiException) {
            dialogsManager.alert(e.uiMessage)
        }
    }

    fun delete(
        folderDb: TaskFolderDb,
        dialogsManager: DialogsManager,
        onDelete: () -> Unit,
    ): Unit = launchExIo {

        if (folderDb.isToday) {
            dialogsManager.alert("It's impossible to delete \"Today\" folder")
            return@launchExIo
        }

        if (TaskDb.selectAsc().any { it.folder_id == folderDb.id }) {
            dialogsManager.alert("The folder must be empty before deletion")
            return@launchExIo
        }

        dialogsManager.confirmation(
            message = "Are you sure you want to delete \"${folderDb.name}\" folder",
            buttonText = "Delete",
        ) {
            launchExIo {
                folderDb.delete()
                onUi { onDelete() }
            }
        }
    }
}
