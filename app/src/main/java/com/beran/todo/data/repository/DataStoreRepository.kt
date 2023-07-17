package com.beran.todo.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.beran.todo.data.models.Priority
import com.beran.todo.utils.Constant.PREFERENCES_KEY
import com.beran.todo.utils.Constant.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object PreferencesKey {
        val sortKey = stringPreferencesKey(PREFERENCES_KEY)
    }

    private val dataStore = context.dataStore

    suspend fun persistSortState(priority: Priority) {
        dataStore.edit { preference ->
            preference[PreferencesKey.sortKey] = priority.name
        }
    }

    val readSortState: Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            val sortState = preferences[PreferencesKey.sortKey] ?: Priority.None.name
            sortState
        }
}