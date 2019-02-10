package com.example.anagy.mrbs.observer

interface Observer {
    enum class ObserverStatus {
        FETCH, MSG
    }

    fun update(status: ObserverStatus, obj: Any)
}