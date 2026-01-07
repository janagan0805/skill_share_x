package com.example.skillsharex.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.data.model.Session
import com.example.skillsharex.network.ApiClient
import kotlinx.coroutines.launch

class SessionViewModel : ViewModel() {

    var sessions = mutableStateOf<List<Session>>(emptyList())
        private set

    var selectedSession = mutableStateOf<Session?>(null)
        private set

    fun loadSessions() {
        viewModelScope.launch {
            try {
                val response = ApiClient.sessionApi.getSessions()
                if (response.isSuccessful) {
                    sessions.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadSessionDetail(sessionId: Int) {
        viewModelScope.launch {
            try {
                val response = ApiClient.sessionApi.getSessionDetail(sessionId)
                if (response.isSuccessful) {
                    selectedSession.value = response.body()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
