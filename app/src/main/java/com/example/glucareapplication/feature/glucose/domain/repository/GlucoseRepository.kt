package com.example.glucareapplication.feature.glucose.domain.repository

import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import com.example.glucareapplication.feature.glucose.domain.model.PredictResponse
import com.example.glucareapplication.feature.glucose.domain.model.SavePredictResponse
import okhttp3.MultipartBody


interface GlucoseRepository {
    suspend fun getHistories(token:String):HistoriesResponse
    suspend fun postPredict(user:String,file: MultipartBody.Part):PredictResponse
    suspend fun postSavePredict(token: String,image:String,result:String):SavePredictResponse
}