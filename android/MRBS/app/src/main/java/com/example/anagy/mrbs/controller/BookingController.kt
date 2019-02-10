package com.example.anagy.mrbs.controller

import android.content.Context
import com.example.anagy.mrbs.model.Booking
import com.example.anagy.mrbs.observer.Observer
import com.example.anagy.mrbs.repository.BookingRepository
import com.example.anagy.mrbs.service.BookingService

class BookingController(context: Context) {

    companion object : SingletonHolder<BookingController, Context>(::BookingController)

    private var bookingService = BookingService(BookingRepository(context))

    fun getAllBookingsForDate(year: Int, month: Int, day: Int): List<Booking> {
        return bookingService.getAllBookingsForDate(year, month, day)
    }

    fun pushBookings() {
        bookingService.pushBookings()
    }

    fun addBooking(booking: Booking) {
        bookingService.addBooking(booking)
    }

    fun getBookingById(bookingId: Int): Booking {
        return bookingService.getBookingById(bookingId)
    }

    fun deleteBooking(bookingId: Int) {
        bookingService.deleteBooking(bookingId)
    }

    fun updateBooking(booking: Booking) {
        bookingService.updateBooking(booking)
    }

    fun subscribe(observer: Observer) {
        bookingService.attach(observer)
    }
}