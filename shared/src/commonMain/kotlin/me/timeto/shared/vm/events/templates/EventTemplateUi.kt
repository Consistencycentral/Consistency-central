package habitarc.shared.vm.events.templates

import habitarc.shared.UnixTime
import habitarc.shared.db.EventTemplateDb
import habitarc.shared.textFeatures

data class EventTemplateUi(
    val eventTemplateDb: EventTemplateDb,
) {

    val shortText: String =
        eventTemplateDb.text.textFeatures().textNoFeatures.let {
            if (it.length <= 17) it else it.substring(0..14) + ".."
        }

    val timeForEventForm: Int =
        UnixTime().localDayStartTime() + eventTemplateDb.daytime
}
