package com.alamkanak.weekview

internal class HeaderRowHeightUpdater<T>(
    private val config: WeekViewConfigWrapper,
    private val cache: EventCache<T>
) : Updater {

    private var previousHorizontalOrigin: Float? = null

    override val isRequired: Boolean
        get() {
            return true
            // Fixme
            /*val currentTimeColumnWidth = config.timeTextWidth + config.timeColumnPadding * 2
            val didTimeColumnChange = currentTimeColumnWidth != config.timeColumnWidth
            val didScrollHorizontally = previousHorizontalOrigin != config.currentOrigin.x
            return didTimeColumnChange || didScrollHorizontally*/
        }

    override fun update(drawingContext: DrawingContext) {
        previousHorizontalOrigin = config.currentOrigin.x
        config.timeColumnWidth = config.timeTextWidth + config.timeColumnPadding * 2
        refreshHeaderHeight(drawingContext)
    }

    private fun refreshHeaderHeight(drawingContext: DrawingContext) {
        val dateRange = drawingContext.dateRangeWithStartPixels.map { it.first }
        val visibleEvents = cache[dateRange].filter { it.isAllDay }
        config.hasEventInHeader = visibleEvents.isNotEmpty()
        config.refreshHeaderHeight()
    }
}
