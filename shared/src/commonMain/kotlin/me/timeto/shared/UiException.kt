package habitarc.shared

class UiException(
    val uiMessage: String,
) : Exception(uiMessage)
