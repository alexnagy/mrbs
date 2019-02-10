//package com.example.anagy.mrbs.service
//
//import com.loopj.android.http.AsyncHttpResponseHandler
//import com.example.anagy.mrbs.observer.Observer.ObserverStatus
//import cz.msebera.android.httpclient.Header
//
//open class BookingServiceAsyncHttpResponseHandler(private val service: BookingService) : AsyncHttpResponseHandler() {
//
////    override fun onSuccess(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?) {
////    }
//
//    override fun onFailure(statusCode: Int, headers: Array<Header>?, responseBody: ByteArray?, error: Throwable?) {
//        service.notifyObservers(ObserverStatus.MSG, "Offline mode. Can't connect to server.")
//    }
//}