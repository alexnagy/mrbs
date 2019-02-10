package com.example.anagy.mrbs.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.new_booking.*
import java.text.SimpleDateFormat
import java.util.*
import android.widget.DatePicker
import android.widget.TimePicker
import com.example.anagy.mrbs.R
import com.example.anagy.mrbs.controller.BookingController
import com.example.anagy.mrbs.model.Booking
import com.example.anagy.mrbs.observer.Observer
import org.jetbrains.anko.toast
import java.lang.Exception


class NewBooking: FragmentActivity(), DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var id = 0
    var startCalendar = Calendar.getInstance()
    var endCalendar = Calendar.getInstance()
    var selectedRoom = ""
    var timeBool = false
    private var bookingController = BookingController.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_booking)

        date.text = SimpleDateFormat.getDateInstance().format(Date())
        start_time.text = SimpleDateFormat.getTimeInstance(java.text.DateFormat.SHORT).format(Date())
        end_time.text = SimpleDateFormat("HH:mm").format(Date(System.currentTimeMillis() + 1200 * 1000))

        date.setOnClickListener { view ->
            showDatePickerDialog(view)
        }

        start_time.setOnClickListener { view ->
            timeBool = true
            showTimePickerDialog(view)
        }

        end_time.setOnClickListener { view ->
            timeBool = false
            showTimePickerDialog(view)
        }

        room.setOnClickListener { view ->
            showAlertDialogForChoosingRoom(view)
        }

        book_button.setOnClickListener { view ->
            createBooking(view)
        }
    }

    private fun showDatePickerDialog(view: View) {
        val newFragment = DatePickerFragment()
        newFragment.show(supportFragmentManager, "datePicker")
    }

    private fun showTimePickerDialog(view: View) {
        TimePickerFragment().show(supportFragmentManager, "timePicker")
    }

    private fun showAlertDialogForChoosingRoom(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Choose a room")

        val options = arrayOf("Room A", "Room B", "Room C", "Room D", "Room E")
        builder.setItems(options) { dialog, which ->
            room.text = options[which]
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

        date.text = SimpleDateFormat.getDateInstance().format(startCalendar.time)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if(timeBool) {
            startCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            startCalendar.set(Calendar.MINUTE, minute)
            start_time.text = SimpleDateFormat("HH:mm").format(startCalendar.time)
        }
        else {
            endCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            endCalendar.set(Calendar.MINUTE, minute)
            end_time.text = SimpleDateFormat("HH:mm").format(endCalendar.time)
        }
    }

    fun createBooking(view: View) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error!")

        if (startCalendar.compareTo(Calendar.getInstance()) < 0) {
            builder.setMessage("Please select a date and a time in the future!")

            builder.setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
            }

            val dialog = builder.create()
            dialog.show()
        }
        else if(startCalendar.compareTo(endCalendar) >= 0) {

            builder.setMessage("Please select and end time which is after the start time!")

            builder.setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
            }

            val dialog = builder.create()
            dialog.show()
        }
        else if(selectedRoom == "") {
            builder.setMessage("Please select a room!")

            builder.setPositiveButton("OK") { dialog, which ->
                dialog.cancel()
            }

            val dialog = builder.create()
            dialog.show()
        }
        else {
            val booking = Booking(booking_title.text.toString(), startCalendar.get(Calendar.YEAR),
                startCalendar.get(Calendar.MONTH), startCalendar.get(Calendar.DAY_OF_MONTH),
                startCalendar.get(Calendar.HOUR_OF_DAY), startCalendar.get(Calendar.MINUTE),
                endCalendar.get(Calendar.HOUR_OF_DAY), endCalendar.get(Calendar.MINUTE), selectedRoom)

            bookingController.addBooking(booking)

            finish()
        }
    }
}
