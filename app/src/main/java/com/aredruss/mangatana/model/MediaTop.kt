package com.aredruss.mangatana.model

import com.google.gson.annotations.SerializedName

data class MediaTop(
    @SerializedName("top")
    val top: List<MediaResponse>
)
