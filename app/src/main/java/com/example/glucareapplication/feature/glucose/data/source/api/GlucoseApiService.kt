package com.example.glucareapplication.feature.glucose.data.source.api

import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import com.example.glucareapplication.feature.glucose.domain.model.SavePredictResponse
import retrofit2.http.*

interface GlucoseApiService {

    @GET("histories")
    suspend fun getHistories(
        @Header("Authorization") token: String
    ):HistoriesResponse

    @POST("predict")
    @FormUrlEncoded
    suspend fun postSavePredict(
        @Header("Authorization") token: String,
        @Field("image") image : String,
        @Field("result") result : String
    ):SavePredictResponse

    @PUT("/predict")
    suspend fun postEditPredict(
        @Header("Authorization") token: String,
        @Field("id") id : Int,
        @Field("patient_email") email : String
    )
}