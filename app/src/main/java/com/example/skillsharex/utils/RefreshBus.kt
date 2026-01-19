package com.example.skillsharex.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object RefreshBus {

    private val _events = MutableSharedFlow<RefreshEvent>(
        replay = 0,
        extraBufferCapacity = 1
    )

    val events = _events.asSharedFlow()

    fun send(event: RefreshEvent) {
        _events.tryEmit(event)
    }
}

sealed class RefreshEvent {
    object ProfileUpdated : RefreshEvent()
}
