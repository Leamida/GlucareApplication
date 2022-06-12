package com.example.glucareapplication.feature.glucose.data.repository

import com.example.glucareapplication.feature.glucose.data.source.api.GlucoseApiService
import com.example.glucareapplication.feature.glucose.data.source.api.ModelDoctorApiService
import com.example.glucareapplication.feature.glucose.data.source.api.ModelPatientApiService
import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import com.example.glucareapplication.feature.glucose.domain.model.PredictDoctorResponse
import com.example.glucareapplication.feature.glucose.domain.model.PredictResponse
import com.example.glucareapplication.feature.glucose.domain.model.SavePredictResponse
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

    override suspend fun postPredict(file: MultipartBody.Part): PredictResponse {
            return modelPatientApiService.postPredict(file)
    }

    override suspend fun postDoctorPredict(file: MultipartBody.Part): PredictDoctorResponse {
        return modelDoctorApiService.postPredict(file)
    }

    override suspend fun postSavePredict(
        token: String,
        image: String,
        result: String
    ): SavePredictResponse {
        return glucoseApiService.postSavePredict(token, image, result)
    }
}