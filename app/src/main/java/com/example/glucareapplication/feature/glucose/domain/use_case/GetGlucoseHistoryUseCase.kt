package com.example.glucareapplication.feature.glucose.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import com.example.glucareapplication.feature.glucose.domain.repository.GlucoseRepository
import javax.inject.Inject
import com.example.glucareapplication.core.util.Result
import retrofit2.HttpException
import java.io.IOException

class GetGlucoseHistoryUseCase @Inject constructor(
    private val glucoseRepository: GlucoseRepository
) {
    private val _glucoseHistories = MutableLiveData<HistoriesResponse>()
    operator fun invoke(token: String): LiveData<Result<HistoriesResponse>> = liveData {
        emit(Result.Loading)
        try {
            val historiesResponse = glucoseRepository.getHistories(token)
            _glucoseHistories.value = historiesResponse
            val tempData: LiveData<Result<HistoriesResponse>> = _glucoseHistories.map { map -> Result.Success(map) }
            emitSource(tempData)

        } catch (e: HttpException) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        } catch (e: IOException) {
            emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
        }
    }
}