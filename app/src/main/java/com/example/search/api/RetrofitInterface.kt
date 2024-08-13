package com.example.search.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface RetrofitInterface {
    @GET("v2/search/image")
    fun searchImage(
        @Header("Authorization") key: String,
        @Query("query") query : String,
        @Query("sort") sort : String = "recency",
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 80
    ) : Call<APIResponse>
}
