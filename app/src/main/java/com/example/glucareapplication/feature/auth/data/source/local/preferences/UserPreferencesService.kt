package com.example.glucareapplication.feature.auth.data.source.local.preferences

import kotlinx.coroutines.flow.Flow

interface UserPreferencesService {
    fun getToken(): Flow<String?>
    suspend fun setToken(token: String?)

    fun getUser(): Flow<List<String?>>
    suspend fun setUser(token: String?)
}