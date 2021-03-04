package com.aredruss.mangatana.data.model

import com.google.gson.annotations.SerializedName

data class MediaResponse(
    @SerializedName("mal_id")
    val malId: Long,
    @SerializedName("url")
    val url: String,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("synopsis")
    val synopsis: String,
    @SerializedName("chapters")
    val chapters: Int,
    @SerializedName("score")
    val score: Double,
    @SerializedName("genres")
    val genreList: List<Genre>,
    @SerializedName("authors")
    val authorList: List<Author>
)