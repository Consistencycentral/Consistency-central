package habitarc.shared.vm.tasks

import kotlinx.coroutines.flow.MutableStateFlow
import habitarc.shared.Cache
import habitarc.shared.DayBarsUi
import habitarc.shared.DaytimeUi
import habitarc.shared.db.Goal2Db
import habitarc.shared.db.TaskDb
import habitarc.shared.launchExIo
import habitarc.shared.textFeatures
import habitarc.shared.toTimerHintNote
import habitarc.shared.vm.Vm

class TaskTimerVm(
    taskDb: TaskDb,
) : Vm<TaskTimerVm.State>() {

    data class State(
        val goalsUi: List<GoalUi>
    )

    // todo update list on changes
    override val state = MutableStateFlow(
        State(
            goalsUi = Cache.goals2Db.map { goalDb ->
                GoalUi(
                    goalDb = goalDb,
                    taskDb = taskDb,
                )
            },
        )
    )

    ///

    class GoalUi(
        val goalDb: Goal2Db,
        val taskDb: TaskDb,
    ) {

        val text: String =
            goalDb.name.textFeatures().textUi()

        val timerHintsUi: List<TimerHintUi> =
            goalDb.buildTimerHintsOrDefault().map { seconds ->
                TimerHintUi(
                    seconds = seconds,
                    onTap = {
                        launchExIo {
                            taskDb.startInterval(
                                timer = seconds,
                                goalDb = goalDb,
                            )
                        }
                    },
                )
            }

        fun start(timer: Int) {
            launchExIo {
                taskDb.startInterval(
                    timer = timer,
                    goalDb = goalDb,
                )
            }
        }

        fun startUntil(daytimeUi: DaytimeUi) {
            launchExIo {
                taskDb.startInterval(
                    timer = daytimeUi.calcTimer(),
                    goalDb = goalDb,
                )
            }
        }

        fun startRestOfGoal() {
            launchExIo {
                val goalStats = DayBarsUi.buildToday().buildGoalStats(goalDb)
                taskDb.startInterval(
                    timer = goalStats.calcRestOfGoal(),
                    goalDb = goalDb,
                )
            }
        }
    }

    class TimerHintUi(
        val seconds: Int,
        val onTap: () -> Unit,
    ) {
        val title: String =
            seconds.toTimerHintNote(isShort = true)
    }
}
