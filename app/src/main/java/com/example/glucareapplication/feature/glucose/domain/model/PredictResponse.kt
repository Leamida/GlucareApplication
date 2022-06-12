package com.example.glucareapplication.feature.glucose.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PredictResponse(

	@field:SerializedName("image_eye")
	val imageEye: String,

	@field:SerializedName("predict_eye")
	val predictEye: List<String>
) : Parcelable
