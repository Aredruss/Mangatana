package com.aredruss.mangatana.api

import com.aredruss.mangatana.model.JikanApiResponse
import com.aredruss.mangatana.model.MediaResponse
import com.aredruss.mangatana.model.MediaTop
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanService {
    @GET("/v4/{type}")
    suspend fun searchByTitle(
        @Path("type") type: String,
        @Query("q") title: String,
        @Query("sfw") sfw:Boolean?
    ): JikanApiResponse<ArrayList<MediaResponse>>

    @GET("/v3/top/{type}/{page_num}/")
    suspend fun getTopMedia(
        @Path("type") type: String,
        @Path("page_num") pageNum: Int
    ): MediaTop

    @GET("/v3/{type}/{id}")
    suspend fun getMediaById(
        @Path("type") type: String,
        @Path("id") id: Long
    ): MediaResponse
}
