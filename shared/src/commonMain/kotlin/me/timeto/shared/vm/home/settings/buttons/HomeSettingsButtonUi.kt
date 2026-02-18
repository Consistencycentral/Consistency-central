package habitarc.shared.vm.home.settings.buttons

import habitarc.shared.ColorRgba
import habitarc.shared.HomeButtonSort
import habitarc.shared.vm.home.buttons.homeButtonsCellsCount
import kotlin.math.absoluteValue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class HomeSettingsButtonUi(
    val type: HomeSettingsButtonType,
    val sort: HomeButtonSort,
    val colorRgba: ColorRgba,
    val spacing: Float,
    val cellWidth: Float,
    val rowHeight: Float,
) {

    @OptIn(ExperimentalUuidApi::class)
    val id: String =
        Uuid.random().toString()

    val offsetX: Float =
        (sort.cellIdx * cellWidth) + (sort.cellIdx * spacing)

    val offsetY: Float = run {
        val extraRows: Int = if (sort.rowIdx >= HomeButtonSort.visibleRows) 2 else 1
        (sort.rowIdx + extraRows) * rowHeight
    }

    val fullWidth: Float =
        ((cellWidth * sort.size) + ((sort.size - 1) * spacing)).absoluteValue

    val resizeLeftMinOffset: Float =
        -((cellWidth * (sort.size - 1)) + (spacing * (sort.size - 1)))

    val resizeLeftMaxOffset: Float =
        (cellWidth * sort.cellIdx) + (spacing * sort.cellIdx)

    val resizeRightMinOffset: Float =
        -((cellWidth * (sort.size - 1)) + (spacing * (sort.size - 1)))

    val resizeRightMaxOffset: Float = run {
        val cellsRight: Int =
            homeButtonsCellsCount - (sort.cellIdx + sort.size)
        (cellWidth * cellsRight) + (spacing * cellsRight)
    }
}
