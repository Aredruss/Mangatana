package com.aredruss.mangatana.model

import com.google.gson.annotations.SerializedName

data class Genre(
    @SerializedName("mal_id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("type")
    val type: String
)
