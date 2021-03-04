package com.aredruss.mangatana.ui.media.dialog

import com.aredruss.mangatana.data.model.MediaResponse

sealed class MediaInfoState {
    object InProgress : MediaInfoState()
    class Success(val media: MediaResponse) : MediaInfoState()
    class Error(val error: Throwable) : MediaInfoState()
}
