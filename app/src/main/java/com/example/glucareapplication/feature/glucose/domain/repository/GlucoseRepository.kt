package com.example.glucareapplication.feature.glucose.domain.repository

import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import com.example.glucareapplication.feature.glucose.domain.model.PredictDoctorResponse
import com.example.glucareapplication.feature.glucose.domain.model.PredictResponse
import com.example.glucareapplication.feature.glucose.domain.model.SavePredictResponse
import okhttp3.MultipartBody


interface GlucoseRepository {
    suspend fun getHistories(token:String):HistoriesResponse
    suspend fun postPredict(file: MultipartBody.Part):PredictResponse
    suspend fun postDoctorPredict(file: MultipartBody.Part):PredictDoctorResponse
    suspend fun postSavePredict(token: String,image:String,result:String):SavePredictResponse
}