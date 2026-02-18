package habitarc.shared.vm.home.settings.buttons

import habitarc.shared.db.Goal2Db
import habitarc.shared.textFeatures

sealed class HomeSettingsButtonType {

    data class Goal(
        val goalDb: Goal2Db,
    ) : HomeSettingsButtonType() {

        val note: String =
            goalDb.name.textFeatures().textNoFeatures
    }

    object Empty : HomeSettingsButtonType()
}
