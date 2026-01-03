package com.example.skillsharex.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.skillsharex.model.community.CommunityPost
import com.example.skillsharex.model.community.UpcomingEvent
import com.example.skillsharex.network.CommunityService
import com.example.skillsharex.repository.CommunityRepository
import kotlinx.coroutines.launch

class CommunityViewModel : ViewModel() {

    private val repository =
        CommunityRepository(CommunityService.api)

    /* ---------- UI STATE ---------- */

    var feedPosts by mutableStateOf<List<CommunityPost>>(emptyList())
        private set

    var upcomingEvents by mutableStateOf<List<UpcomingEvent>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    /* ---------- LOAD COMMUNITY FEED ---------- */

    fun loadCommunityFeed() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = repository.fetchCommunityFeed()
                feedPosts = response.feedPosts
                upcomingEvents = response.upcomingEvents
            } catch (e: Exception) {
                Log.e("CommunityViewModel", "Failed to load community feed", e)
                errorMessage = "Unable to load community feed"
                feedPosts = emptyList()
                upcomingEvents = emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    /* ---------- OPTIONAL: REFRESH ---------- */

    fun refresh() {
        loadCommunityFeed()
    }

    /* ---------- OPTIONAL: FILTER (UI ONLY) ---------- */

    fun filterByType(type: String): List<CommunityPost> {
        return if (type.equals("all", ignoreCase = true)) {
            feedPosts
        } else {
            feedPosts.filter { it.postType.equals(type, ignoreCase = true) }
        }
    }
}
