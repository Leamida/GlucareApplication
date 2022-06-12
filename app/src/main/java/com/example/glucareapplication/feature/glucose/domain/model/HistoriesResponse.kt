package com.example.glucareapplication.feature.glucose.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoriesResponse(

	@field:SerializedName("predict_results")
	val predictResults: List<PredictResultsItem>,

	@field:SerializedName("user")
	val user: String
) : Parcelable

@Parcelize
data class PredictResultsItem(

	@field:SerializedName("result")
	val result: String,

	@field:SerializedName("image")
	val image: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("doctor_email")
	val doctorEmail: String,

	@field:SerializedName("patient_email")
	val patientEmail: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("updatedAt")
	val updatedAt: String
) : Parcelable
