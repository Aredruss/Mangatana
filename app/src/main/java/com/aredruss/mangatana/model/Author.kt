package com.aredruss.mangatana.model

import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("name")
    val name: String
)
