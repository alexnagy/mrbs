package com.example.anagy.mrbs.repository

import android.provider.BaseColumns

object Bookings {
    const val DB_NAME = "MRBS_DB"
    const val DB_VERSION = 1

    // Table contents are grouped together in an anonymous object.
    object Booking : BaseColumns {
        const val DB_TABLE = "Bookings"
        const val COLUMN_ID = "Id"
        const val COLUMN_TITLE = "Title"
        const val COLUMN_YEAR = "Year"
        const val COLUMN_MONTH = "Month"
        const val COLUMN_DAY = "Day"
        const val COLUMN_START_HOUR = "StartHour"
        const val COLUMN_START_MIN = "StartMin"
        const val COLUMN_END_HOUR = "EndHour"
        const val COLUMN_END_MIN = "EndMin"
        const val COLUMN_ROOM = "Room"
    }

    object BookingOffline : BaseColumns {
        const val DB_TABLE = "BookingsOffline"
        const val COLUMN_ID = "Id"
        const val COLUMN_TITLE = "Title"
        const val COLUMN_YEAR = "Year"
        const val COLUMN_MONTH = "Month"
        const val COLUMN_DAY = "Day"
        const val COLUMN_START_HOUR = "StartHour"
        const val COLUMN_START_MIN = "StartMin"
        const val COLUMN_END_HOUR = "EndHour"
        const val COLUMN_END_MIN = "EndMin"
        const val COLUMN_ROOM = "Room"
    }
}