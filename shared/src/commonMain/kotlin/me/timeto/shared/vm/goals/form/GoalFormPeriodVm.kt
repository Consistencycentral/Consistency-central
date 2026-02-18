package habitarc.shared.vm.goals.form

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import habitarc.shared.UnixTime
import habitarc.shared.DialogsManager
import habitarc.shared.UiException
import habitarc.shared.db.Goal2Db
import habitarc.shared.vm.Vm

class GoalFormPeriodVm(
    initGoalDbPeriod: Goal2Db.Period,
) : Vm<GoalFormPeriodVm.State>() {

    data class State(
        val selectedDaysOfWeek: Set<Int>,
    ) {

        val title = "Period"
        val doneText = "Done"

        fun buildPeriodOrNull(
            dialogsManager: DialogsManager,
        ): Goal2Db.Period? = try {
            Goal2Db.Period.DaysOfWeek.buildWithValidation(selectedDaysOfWeek)
        } catch (e: UiException) {
            dialogsManager.alert(e.uiMessage)
            null
        }
    }

    override val state = MutableStateFlow(
        State(
            selectedDaysOfWeek = when (initGoalDbPeriod) {
                is Goal2Db.Period.DaysOfWeek -> initGoalDbPeriod.days.toSet()
                else -> (0..6).toSet()
            },
        )
    )

    ///

    val daysOfWeek: List<DayOfWeek> = UnixTime.dayOfWeekNames
        .mapIndexed { dayIdx, dayTitle ->
            DayOfWeek(
                id = dayIdx,
                title = "Every $dayTitle",
            )
        }

    fun toggleDayOfWeek(dayOfWeek: DayOfWeek) {
        state.update { state ->
            val newDayOfWeek: MutableSet<Int> =
                state.selectedDaysOfWeek.toMutableSet()
            if (newDayOfWeek.contains(dayOfWeek.id))
                newDayOfWeek.remove(dayOfWeek.id)
            else
                newDayOfWeek.add(dayOfWeek.id)
            state.copy(selectedDaysOfWeek = newDayOfWeek)
        }
    }

    ///

    data class DayOfWeek(
        val id: Int,
        val title: String,
    )
}
