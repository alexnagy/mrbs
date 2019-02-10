package com.example.anagy.mrbs.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import com.example.anagy.mrbs.R
import com.example.anagy.mrbs.controller.BookingController
import com.example.anagy.mrbs.model.Booking
import com.example.anagy.mrbs.observer.Observer
import kotlinx.android.synthetic.main.view_booking.*
import org.jetbrains.anko.toast
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


class ViewBooking: FragmentActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var startCalendar = Calendar.getInstance()
    private var endCalendar = Calendar.getInstance()
    private var timeBool = false
    private var selectedRoom = ""
    private var bookingController = BookingController.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.view_booking)

        setBookingFields()

        date_view.setOnClickListener { view ->
            showDatePickerDialog(view)
        }

        start_time_view.setOnClickListener { view ->
            timeBool = true
            showTimePickerDialog(view)
        }

        end_time_view.setOnClickListener { view ->
            timeBool = false
            showTimePickerDialog(view)
        }

        room_view.setOnClickListener { view ->
            showAlertDialogForChoosingRoom(view)
        }

        update_button.setOnClickListener {
            onUpdateButtonClicked()
        }

        delete_button.setOnClickListener {
            onDeleteButtonClicked()
        }
    }

    private fun showDatePickerDialog(view: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker2")
    }

    private fun showTimePickerDialog(view: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker2")
    }

    private fun showAlertDialogForChoosingRoom(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose a room")

        val options = arrayOf("Room A", "Room B", "Room C", "Room D", "Room E")
        builder.setItems(options) { dialog, which ->
            room_view.text = options[which]
            selectedRoom = options[which]
            dialog.dismiss()
        }

        builder.setNeutralButton("Cancel") { dialog, which ->
            dialog.cancel()
        }

        val dialog = builder.create()
        dialog.show()
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        startCalendar.set(Calendar.DAY_OF_MONTH, day)
        startCalendar.set(Calendar.MONTH, month)
        startCalendar.set(Calendar.YEAR, year)
        endCalendar.set(Calendar.DAY_OF_MONTH, day)
        endCalendar.set(Calendar.MONTH, month)
        endCalendar.set(Calendar.YEAR, year)

        date_view.text = SimpleDateFormat.getDateInstance().format(startCalendar.time)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if(timeBool) {
            startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            startCalendar.set(Calendar.MINUTE, minute)
            start_time_view.text = SimpleDateFormat("HH:mm").format(startCalendar.time)
        }
        else {
            endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            endCalendar.set(Calendar.MINUTE, minute)
            end_time_view.text = SimpleDateFormat("HH:mm").format(endCalendar.time)
        }
    }

    private fun setBookingFields() {
        val bookingID  = intent.getStringExtra("bookingId")
        val booking = bookingController.getBookingById(bookingID.toInt())

        startCalendar.set(Calendar.DAY_OF_MONTH, booking.day)
        startCalendar.set(Calendar.MONTH, booking.month)
        startCalendar.set(Calendar.YEAR, booking.year)
        startCalendar.set(Calendar.HOUR_OF_DAY, booking.startHour)
        startCalendar.set(Calendar.MINUTE, booking.startMinute)

        endCalendar.set(Calendar.DAY_OF_MONTH, booking.day)
        endCalendar.set(Calendar.MONTH, booking.month)
        endCalendar.set(Calendar.YEAR, booking.year)
        endCalendar.set(Calendar.HOUR_OF_DAY, booking.endHour)
        endCalendar.set(Calendar.MINUTE, booking.endMinute)

        booking_title_view.setText(booking.title)
        date_view.text = SimpleDateFormat.getDateInstance().format(startCalendar.time)
        start_time_view.text = SimpleDateFormat("HH:mm").format(startCalendar.time)
        end_time_view.text = SimpleDateFormat("HH:mm").format(endCalendar.time)
        room_view.text = booking.room
    }

    private fun onUpdateButtonClicked() {
        val bookingID  = intent.getStringExtra("bookingId").toInt()

        if(selectedRoom == "")
            selectedRoom = room_view.text.toString()

        val booking = Booking(bookingID, booking_title_view.text.toString(), startCalendar.get(Calendar.YEAR),
        startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH),
        startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE),
        endCalendar.get(Calendar.HOUR_OF_DAY), endCalendar.get(Calendar.MINUTE), selectedRoom)

        bookingController.updateBooking(booking)
        finish()
    }

    private fun onDeleteButtonClicked() {
        val bookingID  = intent.getStringExtra("bookingId")
        bookingController.deleteBooking(bookingID.toInt())
        finish()
    }
}