package habitarc.shared.vm.tasks.form

import habitarc.shared.db.TaskDb
import habitarc.shared.db.TaskFolderDb

sealed class TaskFormStrategy {

    class NewTask(
        val taskFolderDb: TaskFolderDb,
    ) : TaskFormStrategy()

    class EditTask(
        val taskDb: TaskDb,
    ) : TaskFormStrategy()
}
