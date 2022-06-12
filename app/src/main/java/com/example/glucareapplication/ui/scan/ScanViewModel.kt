package com.example.glucareapplication.ui.scan

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.glucareapplication.core.util.Result
import com.example.glucareapplication.feature.glucose.domain.model.SavePredictResponse
import com.example.glucareapplication.feature.glucose.domain.use_case.PostPredictUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val postPredictUseCase: PostPredictUseCase
) : ViewModel() {
    fun postPredict(token: String,user:String,file: MultipartBody.Part) : LiveData<Result<SavePredictResponse>> = postPredictUseCase(token,user,file)
    }