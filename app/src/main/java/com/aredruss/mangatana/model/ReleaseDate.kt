package com.aredruss.mangatana.model

import com.google.gson.annotations.SerializedName

data class ReleaseDate(
    @SerializedName("from")
    val started: String,
    @SerializedName("to")
    val finished: String
)
