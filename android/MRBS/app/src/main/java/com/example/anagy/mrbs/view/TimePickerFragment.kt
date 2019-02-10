package com.example.anagy.mrbs.view

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.format.DateFormat
import com.example.anagy.mrbs.R
import java.util.*

class TimePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        var listener: TimePickerDialog.OnTimeSetListener? = null
        if(activity!!.localClassName == "view.NewBooking")
            listener = activity as NewBooking
        else if(activity!!.localClassName == "view.ViewBooking")
            listener = activity as ViewBooking

        return TimePickerDialog(activity,
            R.style.TimePickerTheme, listener, hour, minute, DateFormat.is24HourFormat(activity))
    }
}