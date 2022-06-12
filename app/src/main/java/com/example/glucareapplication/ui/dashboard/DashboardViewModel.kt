package com.example.glucareapplication.ui.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.glucareapplication.core.util.Result
import com.example.glucareapplication.feature.glucose.domain.model.HistoriesResponse
import com.example.glucareapplication.feature.glucose.domain.use_case.GetGlucoseHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getGlucoseHistoryUseCase: GetGlucoseHistoryUseCase
):ViewModel(){
    fun getHistories(token:String):LiveData<Result<HistoriesResponse>> = getGlucoseHistoryUseCase(token)
}