package com.example.glucareapplication.feature.glucose.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.glucareapplication.core.util.Result
import com.example.glucareapplication.feature.glucose.data.repository.GlucoseRepositoryImpl
import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import com.example.glucareapplication.feature.glucose.domain.model.PredictResponse
import com.example.glucareapplication.feature.glucose.domain.repository.GlucoseRepository
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostPredictUseCase @Inject constructor(
    private val glucoseRepository: GlucoseRepository
) {
    private val _predictResponse = MutableLiveData<PredictResponse>()
    operator fun invoke(user: String, file: MultipartBody.Part): LiveData<Result<PredictResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val predictResponse = glucoseRepository.postPredict(user, file)
                _predictResponse.value = predictResponse
                val tempData: LiveData<Result<PredictResponse>> =
                    _predictResponse.map { map -> Result.Success(map) }
                emitSource(tempData)
            } catch (e: HttpException) {
                emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: IOException) {
                emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
}