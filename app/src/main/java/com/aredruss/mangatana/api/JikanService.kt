package com.aredruss.mangatana.api

import com.aredruss.mangatana.model.JikanApiResponse
import com.aredruss.mangatana.model.MediaResponse
import com.aredruss.mangatana.model.MediaTop
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanService {
    @GET("/v3/search/{type}")
    suspend fun searchMedia(
        @Path("type") type: String,
        @Query("q") title: String
    ): JikanApiResponse<ArrayList<MediaResponse>>

    @GET("/v3/top/{type}/1/")
    suspend fun exploreMedia(
        @Path("type") type: String
    ): MediaTop

    @GET("/v3/{type}/{id}")
    suspend fun getMediaById(
        @Path("type") type: String,
        @Path("id") id: Long
    ): MediaResponse

    @GET("/v3/genre/{type}/{genre_id}/{page_num}")
    suspend fun getByGenre(
        @Path("type") type: String,
        @Path("genre_id") id: Int,
        @Path("page_num") pageNum: Int,
    ): MediaResponse
}
