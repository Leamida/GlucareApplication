package com.example.glucareapplication.feature.glucose.data.repository

import com.example.glucareapplication.feature.glucose.data.source.api.GlucoseApiService
import com.example.glucareapplication.feature.glucose.data.source.api.ModelDoctorApiService
import com.example.glucareapplication.feature.glucose.data.source.api.ModelPatientApiService
import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import com.example.glucareapplication.feature.glucose.domain.model.PredictResponse
import com.example.glucareapplication.feature.glucose.domain.repository.GlucoseRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class GlucoseRepositoryImpl @Inject constructor(
    private val glucoseApiService: GlucoseApiService,
    private val modelDoctorApiService: ModelDoctorApiService,
    private val modelPatientApiService: ModelPatientApiService
) : GlucoseRepository {
    override suspend fun getHistories(token: String): HistoriesResponse {
        return glucoseApiService.getHistories(token)
    }

    override suspend fun postPredict(user: String, file: MultipartBody.Part): PredictResponse {
        return if (user == "patient") {
            modelPatientApiService.postPredict(file)
        } else {
            modelDoctorApiService.postPredict(file)
        }
    }
}