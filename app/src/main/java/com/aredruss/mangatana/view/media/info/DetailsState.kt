package com.aredruss.mangatana.view.media.info

import com.aredruss.mangatana.model.MediaResponse

sealed class DetailsState {
    object Loading : DetailsState()
    object Empty : DetailsState()
    class Success(val payload: MediaResponse) : DetailsState()
    class Error(val error: Throwable) : DetailsState()
}
