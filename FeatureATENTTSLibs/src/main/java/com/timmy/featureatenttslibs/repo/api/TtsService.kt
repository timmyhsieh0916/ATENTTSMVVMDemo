package com.timmy.featureatenttslibs.repo.api

import com.timmy.base.data.SampleDataFromAPI
import com.timmy.featureatenttslibs.repo.SynthesisRequest
import com.timmy.featureatenttslibs.repo.model.TTSResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface TtsService {

    @Headers("Accept: application/json")
    @GET("AQX_P_02")
    suspend fun getData(
        @Query("api_key") apiKey: String = "6e0a8bca-b4c1-433e-ba1b-f74932279be6",
        @Query("language") language: String = "zh",
        @Query("limit") limit: Int = 10,
        @Query("offset") offset: Int = 1
//        @Query(encoded = true, value = "sort") sort: String = "ImportDate%20desc",
//        @Query("format") format: String = "JSON"
    ): SampleDataFromAPI

    @POST("business/enterprise/api/v1/syntheses/api_token")
    fun synthesizeSpeech(@Body request: SynthesisRequest): Call<TTSResponseBody> // YourResponseType 替換為你的回應數據類
}