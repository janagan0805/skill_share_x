package com.example.skillsharex.utils

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

object RefreshBus {

    // 🔑 replay = 1 ensures late collectors still receive it
    private val _events = MutableSharedFlow<RefreshEvent>(
        replay = 1,
        extraBufferCapacity = 1
    )

    val events = _events.asSharedFlow()

    fun send(event: RefreshEvent) {
        _events.tryEmit(event)
    }

    fun clear() {
        _events.tryEmit(RefreshEvent.None)
    }
}

sealed class RefreshEvent {
    object ProfileUpdated : RefreshEvent()
    object None : RefreshEvent()
}
