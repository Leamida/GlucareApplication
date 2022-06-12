package com.example.glucareapplication.feature.glucose.data.source.api

import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST

interface GlucoseApiService {

    @GET("histories")
    suspend fun getHistories(
        @Header("Authorization") token: String
    ):HistoriesResponse
}