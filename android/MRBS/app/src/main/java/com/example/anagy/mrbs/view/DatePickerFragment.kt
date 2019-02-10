package com.example.anagy.mrbs.view

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import java.util.*
import android.app.DatePickerDialog.OnDateSetListener
import android.util.Log
import com.example.anagy.mrbs.R


class DatePickerFragment : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        var listener: OnDateSetListener? = null
        if(activity!!.localClassName == "view.NewBooking")
            listener = activity as NewBooking
        else if(activity!!.localClassName == "view.ViewBooking")
            listener = activity as ViewBooking

        return DatePickerDialog(activity, R.style.TimePickerTheme, listener, year, month, day)
    }
}