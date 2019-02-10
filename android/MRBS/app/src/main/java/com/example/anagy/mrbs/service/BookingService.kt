package com.example.anagy.mrbs.service

import org.json.JSONException
import com.example.anagy.mrbs.observer.Observer.ObserverStatus
import org.json.JSONArray
import com.example.anagy.mrbs.model.Booking
import com.example.anagy.mrbs.model.BookingParser
import com.example.anagy.mrbs.observer.Observable
import com.example.anagy.mrbs.observer.Observer
import com.example.anagy.mrbs.repository.BookingRepository
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.RequestParams
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import com.loopj.android.http.AsyncHttpResponseHandler


class BookingService(private val repository: BookingRepository) : Observable {

    private val observers = ArrayList<Observer>()

    init {
        httpClient.setMaxRetriesAndTimeout(1, 0)
    }

    override fun attach(observer: Observer) {
        if(!observers.contains(observer))
            observers.add(observer)
    }

    override fun detach(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers(status: ObserverStatus, obj: Any) {
        for (observer in observers) {
            observer.update(status, obj)
        }
    }

    fun getAllBookingsForDate(year: Int, month: Int, day: Int): List<Booking> {
        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                fetchBookings(year, month, day)
            }
        }, HOST, PORT)
        return repository.getBookingsByDate(year, month, day)
    }

    fun fetchBookings(year: Int, month: Int, day: Int) {
        val params = RequestParams()
        params.put("lastBookingId", repository.getLastBookingId())
        httpClient.get("$URL/get", params, object: AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                try {
                    val obj = JSONArray(String(responseBody!!))
                    val bookings = ArrayList<Booking>()
                    for (i in 0 until obj.length()) {
                        bookings.add(BookingParser.parse(obj.getJSONObject(i)))
                    }
                    repository.set(bookings)
                    notifyObservers(ObserverStatus.FETCH, repository.getBookingsByDate(year, month, day))

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?,
                                   error: Throwable?) {}
        })
    }

    fun pushBookings() {
        InternetCheck(object : InternetCheck.Consumer {
            override fun accept(internet: Boolean?) {
                if(internet!!) {
                val bookigsOffline = repository.getOffline()
                for(booking in bookigsOffline) {
                    try {
                        val obj = booking.toJSON()
                        println(obj)
                        val params = RequestParams()
                        params.put("booking", obj)
                        httpClient.post("$URL/add", params, object: AsyncHttpResponseHandler() {
                            override fun onSuccess(statusCode: Int, headers: Array<out Header>?,
                                                   responseBody: ByteArray?) {
                                val respObj = JSONObject(String(responseBody!!))
                                if(respObj.optInt("Id") == -1)
                                {
                                    val message = "Booking \"${booking.title}\" from " +
                                            "${booking.day}/${booking.month+1}/${booking.year} is overlapping a previously " +
                                            "added booking, thus it won't be added!"
                                    notifyObservers(ObserverStatus.MSG, message)
                                }
                                repository.deleteLocalBooking(booking.id)
                            }

                            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?,
                                                   error: Throwable?) {}
                        })
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            }}
        }, HOST, PORT)
    }

    fun addBooking(booking: Booking) {
        try {
            val obj = booking.toJSON()
            println(obj)
            val params = RequestParams()
            params.put("booking", obj)
            httpClient.post("$URL/add", params, object: AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    val respObj = JSONObject(String(responseBody!!))
                    if(respObj.optInt("Id") == -1)
                    {
                        val message = "Booking \"${booking.title}\" from " +
                                "${booking.day}/${booking.month+1}/${booking.year} is overlapping a previously " +
                                "added booking, thus it won't be added!"
                        notifyObservers(ObserverStatus.MSG, message)
                    }
                    else
                        notifyObservers(ObserverStatus.MSG, "Booking added successfully!")
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    repository.insertLocalBooking(booking)
                    notifyObservers(ObserverStatus.MSG, "Offline. Booking saved locally!")
                }
            })
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun deleteBooking(bookingId: Int) {
        try {
            val params = RequestParams()
            params.put("bookingId", bookingId)
            httpClient.post("$URL/delete", params, object: AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    try {
                        val respObj = JSONObject(String(responseBody!!))
                        if(respObj.optInt("RowCount") == 1)
                            repository.deleteBooking(bookingId)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    notifyObservers(ObserverStatus.MSG, "Can not delete while offline!")
                }
            })
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun updateBooking(booking: Booking) {
        try {
            val obj = booking.toJSON()
            println(obj)
            val params = RequestParams()
            params.put("booking", obj)
            httpClient.post("$URL/update", params, object: AsyncHttpResponseHandler() {
                override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                    try {
                        val respObj = JSONObject(String(responseBody!!))
                        if(respObj.optInt("RowCount") == 1)
                            repository.updateBooking(booking)
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    notifyObservers(ObserverStatus.MSG, "Can not update while offline!")
                }
            })
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun getBookingById(bookingId: Int): Booking {
        return repository.getBookingById(bookingId)
    }

    companion object {
        private const val HOST = "172.30.116.50"
        private const val PORT = 8081
        private const val URL = "http://$HOST:$PORT"
        private val httpClient = AsyncHttpClient()
    }
}

//class OverlappingBookingException(message: String): Exception(message)