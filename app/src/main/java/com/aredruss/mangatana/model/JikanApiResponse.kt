package com.aredruss.mangatana.model

import com.google.gson.annotations.SerializedName

data class JikanApiResponse<T>(
    @SerializedName("data")
    val results: T
)
