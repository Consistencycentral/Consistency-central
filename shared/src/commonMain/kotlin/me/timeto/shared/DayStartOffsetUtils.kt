package habitarc.shared

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import habitarc.shared.db.KvDb
import habitarc.shared.db.KvDb.Companion.asDayStartOffsetSeconds

object DayStartOffsetUtils {

    fun getLocalUtcOffsetCached(): Int =
        localUtcOffset - getOffsetSecondsCached()

    suspend fun getOffsetSeconds(): Int =
        KvDb.KEY.DAY_START_OFFSET_SECONDS.selectOrNull().asDayStartOffsetSeconds()

    fun getOffsetSecondsCached(): Int =
        KvDb.KEY.DAY_START_OFFSET_SECONDS.selectOrNullCached().asDayStartOffsetSeconds()

    suspend fun getToday(): Int = calcDay(
        time = time(),
        dayStartOffsetSeconds = getOffsetSeconds(),
    )

    fun calcDay(
        time: Int,
        dayStartOffsetSeconds: Int,
    ): Int {
        val localUtcOffsetWithDayStart: Int =
            localUtcOffset - dayStartOffsetSeconds
        return UnixTime(
            time = time,
            utcOffset = localUtcOffsetWithDayStart,
        ).localDay
    }

    fun buildTodayFlow(): Flow<Int> = combine(
        KvDb.KEY.DAY_START_OFFSET_SECONDS.selectOrNullFlow().distinctUntilChanged(),
        TimeFlows.eachMinuteSecondsFlow,
    ) { kvDb, nowTime ->
        calcDay(
            time = nowTime,
            dayStartOffsetSeconds = kvDb.asDayStartOffsetSeconds(),
        )
    }.distinctUntilChanged()
}
