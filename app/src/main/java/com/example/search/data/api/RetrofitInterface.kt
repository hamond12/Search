package com.example.search.data.api

import com.example.search.data.model.ImageResponse
import com.example.search.data.model.VideoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("v2/search/image")
    fun searchImage(
        @Header("Authorization") key: String,
        @Query("query") query : String,
        @Query("sort") sort : String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ) : Call<ImageResponse>

    @GET("v2/search/vclip")
    fun searchVideo(
        @Header("Authorization") apiKey: String,
        @Query("query") query: String,
        @Query("sort") sort: String,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Call<VideoResponse>
}
