package habitarc.shared

import kotlinx.coroutines.flow.MutableSharedFlow
import habitarc.shared.db.Goal2Db
import habitarc.shared.db.IntervalDb

data class LiveActivity(
    val intervalDb: IntervalDb,
) {

    companion object {

        // Not StateFlow to reschedule same data object
        val flow = MutableSharedFlow<LiveActivity?>()
    }

    ///

    val goalDb: Goal2Db =
        intervalDb.selectGoalDbCached()

    val dynamicIslandTitle: String =
        (intervalDb.note ?: goalDb.name).textFeatures().textNoFeatures
}
