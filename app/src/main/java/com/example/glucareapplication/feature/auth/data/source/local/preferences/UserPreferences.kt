package com.example.glucareapplication.feature.auth.data.source.local.preferences

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("user")

class UserPreferences @Inject constructor(
    @ApplicationContext context: Context
) : UserPreferencesService {

    private val userDataStore = context.dataStore

    private val USER_TOKEN = stringPreferencesKey("user_token")
    private val USER = stringPreferencesKey("user")

    override fun getToken(): Flow<String> = userDataStore.data.map {
            it[USER_TOKEN] ?: ""
    }

    override suspend fun setToken(token: String?) {
        if (token != null) {
            userDataStore.edit {
                it[USER_TOKEN] = token
            }
        } else {
            userDataStore.edit {
                it.clear()
            }
        }
    }

    override fun getUser(): Flow<List<String>> = userDataStore.data.map {
       listOf(
           it[USER] ?: "",
           it[USER_TOKEN] ?: ""
       )
    }

    override suspend fun setUser(user: String?) {
        if (user != null) {
            userDataStore.edit {
                it[USER] = user
            }
        } else {
            userDataStore.edit {
                it.clear()
            }
        }
    }
}