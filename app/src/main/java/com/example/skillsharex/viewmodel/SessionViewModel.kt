package com.example.skillsharex.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.data.model.Session
import com.example.skillsharex.network.AuthApiClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SessionViewModel : ViewModel() {

    // --- Sessions list state ---
    private val _sessions = MutableStateFlow<List<Session>>(emptyList())
    val sessions: StateFlow<List<Session>> = _sessions.asStateFlow()

    // --- Loading state ---
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // --- Selected session detail ---
    private val _selectedSession = MutableStateFlow<Session?>(null)
    val selectedSession: StateFlow<Session?> = _selectedSession.asStateFlow()

    // --- Load all sessions ---
    fun loadSessions() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = AuthApiClient.api.getSessions()

                if (response.isSuccessful && response.body()?.status == true) {
                    _sessions.value = response.body()?.data ?: emptyList()
                } else {
                    _sessions.value = emptyList()
                }

            } catch (e: Exception) {
                e.printStackTrace()
                _sessions.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    // --- Load single session detail ---
    fun loadSessionDetail(sessionId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val response = AuthApiClient.api.getSessionDetail(sessionId)
                if (response.isSuccessful && response.body()?.status == true) {
                    _selectedSession.value = response.body()?.data
                } else {
                    _selectedSession.value = null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _selectedSession.value = null
            } finally {
                _isLoading.value = false
            }
        }
    }


    // Optional cleanup
    fun clearSelectedSession() {
        _selectedSession.value = null
    }
}
