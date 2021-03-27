package com.aredruss.mangatana.view.media.info

import com.aredruss.mangatana.data.database.MediaDb
import com.aredruss.mangatana.model.MediaResponse

sealed class DetailsState {
    object Loading : DetailsState()
    class Success(val payload: MediaResponse, val localEntry: MediaDb?) : DetailsState()
    class Error(val error: Throwable) : DetailsState()
}
