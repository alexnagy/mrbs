package com.example.anagy.mrbs.repository

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.anagy.mrbs.model.Booking

class BookingRepository(context: Context) {
    private val dbHelper: DatabaseHelper = DatabaseHelper(context)
    private val db: SQLiteDatabase by lazy { dbHelper.writableDatabase }

    fun get(): List<Booking> {
        val bookingsList = ArrayList<Booking>()
        val cursor = db.rawQuery("select * from ${Bookings.Booking.DB_TABLE}", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val year = cursor.getInt(cursor.getColumnIndex("Year"))
                val month = cursor.getInt(cursor.getColumnIndex("Month"))
                val day = cursor.getInt(cursor.getColumnIndex("Day"))
                val startHour = cursor.getInt(cursor.getColumnIndex("StartHour"))
                val startMin = cursor.getInt(cursor.getColumnIndex("StartMin"))
                val endHour = cursor.getInt(cursor.getColumnIndex("EndHour"))
                val endMin = cursor.getInt(cursor.getColumnIndex("EndMin"))
                val room = cursor.getString(cursor.getColumnIndex("Room"))

                val booking = Booking(id, title, year, month, day, startHour, startMin, endHour, endMin, room)

                bookingsList.add(booking)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return bookingsList
    }

    fun getOffline(): List<Booking> {
        val bookingsList = ArrayList<Booking>()
        val cursor = db.rawQuery("select * from ${Bookings.BookingOffline.DB_TABLE}", null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val year = cursor.getInt(cursor.getColumnIndex("Year"))
                val month = cursor.getInt(cursor.getColumnIndex("Month"))
                val day = cursor.getInt(cursor.getColumnIndex("Day"))
                val startHour = cursor.getInt(cursor.getColumnIndex("StartHour"))
                val startMin = cursor.getInt(cursor.getColumnIndex("StartMin"))
                val endHour = cursor.getInt(cursor.getColumnIndex("EndHour"))
                val endMin = cursor.getInt(cursor.getColumnIndex("EndMin"))
                val room = cursor.getString(cursor.getColumnIndex("Room"))

                val booking = Booking(id, title, year, month, day, startHour, startMin, endHour, endMin, room)

                bookingsList.add(booking)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return bookingsList
    }

    fun set(bookings: List<Booking>) {
        for(booking in bookings) {
            val values = ContentValues()

            values.put("Id", booking.id)
            values.put("Title", booking.title)
            values.put("Year", booking.year)
            values.put("Month", booking.month)
            values.put("Day", booking.day)
            values.put("StartHour", booking.startHour)
            values.put("StartMin", booking.startMinute)
            values.put("EndHour", booking.endHour)
            values.put("EndMin", booking.endMinute)
            values.put("Room", booking.room)

            insert(values)
        }
    }

    fun getBookingsByDate(_year: Int, _month: Int, _day: Int): List<Booking> {
        val bookingsList = ArrayList<Booking>()
        var cursor = db.rawQuery("select * from ${Bookings.Booking.DB_TABLE} where Year=$_year and Month=$_month " +
                "and Day=$_day",null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val year = cursor.getInt(cursor.getColumnIndex("Year"))
                val month = cursor.getInt(cursor.getColumnIndex("Month"))
                val day = cursor.getInt(cursor.getColumnIndex("Day"))
                val startHour = cursor.getInt(cursor.getColumnIndex("StartHour"))
                val startMin = cursor.getInt(cursor.getColumnIndex("StartMin"))
                val endHour = cursor.getInt(cursor.getColumnIndex("EndHour"))
                val endMin = cursor.getInt(cursor.getColumnIndex("EndMin"))
                val room = cursor.getString(cursor.getColumnIndex("Room"))

                val booking = Booking(id, title, year, month, day, startHour, startMin, endHour, endMin, room)

                bookingsList.add(booking)
            } while (cursor.moveToNext())
        }
        cursor.close()

        cursor = db.rawQuery("select * from ${Bookings.BookingOffline.DB_TABLE} where Year=$_year and Month=$_month " +
                "and Day=$_day",null)
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val title = cursor.getString(cursor.getColumnIndex("Title"))
                val year = cursor.getInt(cursor.getColumnIndex("Year"))
                val month = cursor.getInt(cursor.getColumnIndex("Month"))
                val day = cursor.getInt(cursor.getColumnIndex("Day"))
                val startHour = cursor.getInt(cursor.getColumnIndex("StartHour"))
                val startMin = cursor.getInt(cursor.getColumnIndex("StartMin"))
                val endHour = cursor.getInt(cursor.getColumnIndex("EndHour"))
                val endMin = cursor.getInt(cursor.getColumnIndex("EndMin"))
                val room = cursor.getString(cursor.getColumnIndex("Room"))

                val booking = Booking(id, title, year, month, day, startHour, startMin, endHour, endMin, room)

                bookingsList.add(booking)
            } while (cursor.moveToNext())
        }
        cursor.close()

        return bookingsList
    }

    fun getBookingById(_id: Int): Booking {
        Log.i("ALEX", _id.toString())
        var cursor = db.rawQuery("select * from ${Bookings.Booking.DB_TABLE} where Id=$_id",null)
        var booking: Booking
        if(cursor.moveToFirst()) {

            val title = cursor.getString(cursor.getColumnIndex("Title"))
            val year = cursor.getInt(cursor.getColumnIndex("Year"))
            val month = cursor.getInt(cursor.getColumnIndex("Month"))
            val day = cursor.getInt(cursor.getColumnIndex("Day"))
            val startHour = cursor.getInt(cursor.getColumnIndex("StartHour"))
            val startMin = cursor.getInt(cursor.getColumnIndex("StartMin"))
            val endHour = cursor.getInt(cursor.getColumnIndex("EndHour"))
            val endMin = cursor.getInt(cursor.getColumnIndex("EndMin"))
            val room = cursor.getString(cursor.getColumnIndex("Room"))

            booking = Booking(_id, title, year, month, day, startHour, startMin, endHour, endMin, room)

            cursor.close()
        }
        else {
            cursor = db.rawQuery("select * from ${Bookings.BookingOffline.DB_TABLE} where Id=$_id",null)
            cursor.moveToFirst()

            val title = cursor.getString(cursor.getColumnIndex("Title"))
            val year = cursor.getInt(cursor.getColumnIndex("Year"))
            val month = cursor.getInt(cursor.getColumnIndex("Month"))
            val day = cursor.getInt(cursor.getColumnIndex("Day"))
            val startHour = cursor.getInt(cursor.getColumnIndex("StartHour"))
            val startMin = cursor.getInt(cursor.getColumnIndex("StartMin"))
            val endHour = cursor.getInt(cursor.getColumnIndex("EndHour"))
            val endMin = cursor.getInt(cursor.getColumnIndex("EndMin"))
            val room = cursor.getString(cursor.getColumnIndex("Room"))

            booking = Booking(_id, title, year, month, day, startHour, startMin, endHour, endMin, room)

            cursor.close()
        }
        return booking
    }

    fun getLastBookingId(): Long {
        val cursor = db.rawQuery("select Id from ${Bookings.Booking.DB_TABLE} order by Id desc limit 1", null)
        var lastBookingId: Long = 0
        if(cursor.moveToFirst())
            lastBookingId = cursor.getLong(cursor.getColumnIndex("Id"))
        cursor.close()
        return lastBookingId
    }

    fun deleteBooking(bookingId: Int) {
        delete("Id=?", arrayOf(bookingId.toString()))
    }

    fun insertLocalBooking(booking: Booking) {
        val values = ContentValues()

        values.put("Title", booking.title)
        values.put("Year", booking.year)
        values.put("Month", booking.month)
        values.put("Day", booking.day)
        values.put("StartHour", booking.startHour)
        values.put("StartMin", booking.startMinute)
        values.put("EndHour", booking.endHour)
        values.put("EndMin", booking.endMinute)
        values.put("Room", booking.room)

        db.insert(Bookings.BookingOffline.DB_TABLE, "", values)
    }

    fun deleteLocalBooking(bookingId: Int) {
        db.delete(Bookings.BookingOffline.DB_TABLE, "Id=?", arrayOf(bookingId.toString()))
    }

    fun updateBooking(booking: Booking) {
        val values = ContentValues()

        values.put("Title", booking.title)
        values.put("Year", booking.year)
        values.put("Month", booking.month)
        values.put("Day", booking.day)
        values.put("StartHour", booking.startHour)
        values.put("StartMin", booking.startMinute)
        values.put("EndHour", booking.endHour)
        values.put("EndMin", booking.endMinute)
        values.put("Room", booking.room)

        update(values, "Id=?", arrayOf(booking.id.toString()))
    }

    fun insert(values: ContentValues): Long {
        return db.insert(Bookings.Booking.DB_TABLE, "", values)
    }

    fun delete(selection: String, selectionArgs: Array<String>): Int {
        return db.delete(Bookings.Booking.DB_TABLE, selection, selectionArgs)
    }

    fun update(values: ContentValues, selection: String, selectionArgs: Array<String>): Int {
        return db.update(Bookings.Booking.DB_TABLE, values, selection, selectionArgs)
    }

    fun close() {
        db.close()
    }
}