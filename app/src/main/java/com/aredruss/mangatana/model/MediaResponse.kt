package com.aredruss.mangatana.model

import com.google.gson.annotations.SerializedName

data class MediaResponse(
    @SerializedName("mal_id")
    val malId: Long,
    @SerializedName("image_url")
    val imageUrl: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("synopsis")
    val synopsis: String,
    @SerializedName("score")
    val score: Double,
    @SerializedName("genres")
    val genreList: List<Genre>,
    @SerializedName("authors")
    val authorList: List<Author>?,
    @SerializedName("chapters")
    val chapters: Int?,
    @SerializedName("producers")
    val producerList: List<Author>?,
    @SerializedName("episodes")
    val episodes: Int?,
)
