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
    @SerializedName(value = "authors", alternate = ["producers"])
    val authorList: List<Author>?,
    @SerializedName(value = "chapters", alternate = ["episodes"])
    val chapters: Int?,
    @SerializedName(value = "members")
    val viewerCount: Long,
    @SerializedName(value = "published", alternate = ["aired"])
    val releaseDate: ReleaseDate
)
