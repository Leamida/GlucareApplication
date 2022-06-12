package com.example.glucareapplication.feature.glucose.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PredictDoctorResponse(

    @field:SerializedName("image")
    val imageEye: String,

    @field:SerializedName("predict")
    val predictEye: List<String>
) : Parcelable