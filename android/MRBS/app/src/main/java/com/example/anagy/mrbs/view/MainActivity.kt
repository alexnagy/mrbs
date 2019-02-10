package com.example.anagy.mrbs.view

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import com.example.anagy.mrbs.R
import com.example.anagy.mrbs.model.Booking
import com.example.anagy.mrbs.observer.Observer
import com.example.anagy.mrbs.controller.BookingController
import org.jetbrains.anko.toast
import kotlin.collections.ArrayList
import android.view.View


class MainActivity : AppCompatActivity(), Observer {

    private lateinit var calendarView: CalendarView
    private lateinit var bookingsListView: ListView
    private var bookingsList = ArrayList<Booking>()
    private var bookingController = BookingController.getInstance(this)
    private var selectedDate = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        bookingController.subscribe(this)

        val handler = Handler()
        val runnable = object : Runnable {
            override fun run() {
                try {
                    bookingController.pushBookings()
                } catch (e: Exception) {}
                handler.postDelayed(this, 10000)
            }
        }
        handler.postDelayed(runnable, 10000)

        calendarView = findViewById(R.id.calendarView)
        bookingsListView = findViewById(R.id.bookings_list_view)

        calendarView.setOnDateChangeListener { view, year, month, day ->
            selectedDate.set(Calendar.YEAR, year)
            selectedDate.set(Calendar.MONTH, month)
            selectedDate.set(Calendar.DAY_OF_MONTH, day)
            setBookingListForSelectedDate()
        }

        bookingsListView.setOnItemClickListener { parent, view, position, id ->
            viewBooking(bookingsList[position].id)
        }

        fab.setOnClickListener { view ->
            val intent = Intent(this, NewBooking::class.java)
            startActivity(intent)
        }

        setBookingListForSelectedDate()
    }

    override fun onStart() {
        super.onStart()

        val progressBar: ProgressBar = findViewById(R.id.progress_bar)

        progressBar.visibility = View.GONE
    }

    override fun onPostResume() {
        super.onPostResume()
        setBookingListForSelectedDate()
    }

    private fun setBookingListForSelectedDate() {
        val year = selectedDate.get(Calendar.YEAR)
        val month = selectedDate.get(Calendar.MONTH)
        val day = selectedDate.get(Calendar.DAY_OF_MONTH)

        bookingsList = bookingController.getAllBookingsForDate(year, month, day) as ArrayList<Booking>

        bookingsListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bookingsList)
    }

    private fun viewBooking(id: Int) {
        val intent = Intent(this, ViewBooking::class.java)
        intent.putExtra("bookingId", id.toString())
        startActivity(intent)
    }

    override fun update(status: Observer.ObserverStatus, obj: Any) {
        if (status === Observer.ObserverStatus.FETCH) {
            val year = selectedDate.get(Calendar.YEAR)
            val month = selectedDate.get(Calendar.MONTH)
            val day = selectedDate.get(Calendar.DAY_OF_MONTH)

            bookingsList = obj as ArrayList<Booking>

            bookingsListView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, bookingsList)

        } else if (status === Observer.ObserverStatus.MSG) {
            val message = obj as String
            toast(message)
        }

    }
}
