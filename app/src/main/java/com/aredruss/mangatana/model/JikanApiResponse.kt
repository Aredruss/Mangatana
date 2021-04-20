package com.aredruss.mangatana.model

import com.google.gson.annotations.SerializedName

data class JikanApiResponse<T>(
    @SerializedName("results")
    val results: T
)
