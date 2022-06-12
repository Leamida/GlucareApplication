package com.example.glucareapplication.feature.glucose.domain.use_case

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.glucareapplication.core.util.Result
import com.example.glucareapplication.feature.glucose.domain.model.SavePredictResponse
import com.example.glucareapplication.feature.glucose.domain.repository.GlucoseRepository
import okhttp3.MultipartBody
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class PostPredictUseCase @Inject constructor(
    private val glucoseRepository: GlucoseRepository
) {

    private val _predictResponse = MutableLiveData<SavePredictResponse>()
    operator fun invoke(token:String, user: String, file: MultipartBody.Part): LiveData<Result<SavePredictResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                if (user == "patient"){
                   val predictResponse = glucoseRepository.postPredict(file)
                    if (predictResponse.imageEye.isEmpty()){
                        Result.Error("Failed when processing image.")
                    }else{
                        val savePredictResponse = glucoseRepository.postSavePredict(token,predictResponse.imageEye,predictResponse.predictEye[0])
                        _predictResponse.value = savePredictResponse
                        val tempData: LiveData<Result<SavePredictResponse>> =
                            _predictResponse.map { map -> Result.Success(map) }
                        emitSource(tempData)
                    }
                }else{
                    val predictResponse = glucoseRepository.postDoctorPredict(file)
                    if (predictResponse.imageEye.isEmpty()){
                        Result.Error("Failed when processing image.")
                    }else{
                        val savePredictResponse = glucoseRepository.postSavePredict(token,predictResponse.imageEye,predictResponse.predictEye[0])
                        _predictResponse.value = savePredictResponse
                        val tempData: LiveData<Result<SavePredictResponse>> =
                            _predictResponse.map { map -> Result.Success(map) }
                        emitSource(tempData)
                    }
                }
            } catch (e: HttpException) {
                emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
            } catch (e: IOException) {
                emit(Result.Error(e.localizedMessage ?: "An unexpected error occurred"))
            }
        }
}