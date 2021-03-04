package com.aredruss.mangatana.data.model

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("mal_id")
    val id: Int,
    @SerializedName("name")
    val name: String
)
