package com.example.glucareapplication.feature.glucose.data.source.api

import com.example.glucareapplication.feature.glucose.domain.model.PredictResponse
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ModelPatientApiService {
    @Multipart
    @POST("/")
    suspend fun postPredict(
        @Part file: MultipartBody.Part
    ): PredictResponse
}