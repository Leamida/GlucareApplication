package com.example.glucareapplication.feature.glucose.domain.repository

import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import com.example.glucareapplication.feature.glucose.domain.model.PredictResponse
import okhttp3.MultipartBody


interface GlucoseRepository {
    suspend fun getHistories(token:String):HistoriesResponse
    suspend fun postPredict(user:String,file: MultipartBody.Part):PredictResponse
}