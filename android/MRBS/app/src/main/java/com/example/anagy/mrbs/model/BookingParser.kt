package com.example.anagy.mrbs.model

import org.json.JSONObject

class BookingParser {
    companion object {
        fun parse(bookingJSON: JSONObject): Booking {
            val id = bookingJSON.optInt("Id")
            val title = bookingJSON.optString("Title")
            val day = bookingJSON.optInt("Day")
            val month = bookingJSON.optInt("Month")
            val year = bookingJSON.optInt("Year")
            val startHour = bookingJSON.optInt("StartHour")
            val startMin = bookingJSON.optInt("StartMin")
            val endHour = bookingJSON.optInt("EndHour")
            val endMin = bookingJSON.optInt("EndMin")
            val room = bookingJSON.optString("Room")

            return Booking(
                id,
                title,
                year,
                month,
                day,
                startHour,
                startMin,
                endHour,
                endMin,
                room
            )
        }
    }
}