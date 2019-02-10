package com.example.anagy.mrbs.observer


interface Observable {
    fun attach(observer: Observer)

    fun detach(observer: Observer)

    fun notifyObservers(status: Observer.ObserverStatus, obj: Any)
}