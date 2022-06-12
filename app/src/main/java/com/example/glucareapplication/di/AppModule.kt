package com.example.glucareapplication.di

import com.example.glucareapplication.BuildConfig
import com.example.glucareapplication.feature.glucose.data.repository.GlucoseRepositoryImpl
import com.example.glucareapplication.feature.glucose.data.source.api.GlucoseApiService
import com.example.glucareapplication.feature.glucose.data.source.api.ModelDoctorApiService
import com.example.glucareapplication.feature.glucose.data.source.api.ModelPatientApiService
import com.example.glucareapplication.feature.glucose.domain.repository.GlucoseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    var BASE_URL = "http://34.123.109.98/"
    var MODEL_DOCTOR_URL = "https://doctor-v4fexjqj4q-et.a.run.app"
    var MODEL_PATIENT_URL = "https://mata-v4fexjqj4q-et.a.run.app"

    @Provides
    @Singleton
    fun provideGlucoseApi(): GlucoseApiService {
        val loggingInterceptor =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(GlucoseApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideModelPatientApi(): ModelPatientApiService {
        val loggingInterceptor =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(MODEL_PATIENT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ModelPatientApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideModelDoctorApi(): ModelDoctorApiService {
        val loggingInterceptor =
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl(MODEL_DOCTOR_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(ModelDoctorApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideGlucoseRepository(api: GlucoseApiService,doctor:ModelDoctorApiService,patient:ModelPatientApiService): GlucoseRepository {
        return GlucoseRepositoryImpl(api,doctor,patient)
    }

//
//    @Provides
//    @Singleton
//    fun provideAuthApi(): AuthApiService {
//        val loggingInterceptor =
//            if (BuildConfig.DEBUG) {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
//            } else {
//                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
//            }
//
//        val client = OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BuildConfig.BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(client)
//            .build()
//        return retrofit.create(AuthApiService::class.java)
//    }
//
//    @Provides
//    @Singleton
//    fun provideAuthRepository(api: AuthApiService): AuthRepositoryService {
//        return AuthRepository(api)
//    }

}