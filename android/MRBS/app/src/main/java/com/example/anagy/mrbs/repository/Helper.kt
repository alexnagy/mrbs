package com.example.anagy.mrbs.repository

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import org.jetbrains.anko.toast

const val SQL_CREATE_BOOKINGS =
    """
    CREATE TABLE IF NOT EXISTS ${Bookings.Booking.DB_TABLE} (
        ${Bookings.Booking.COLUMN_ID} INTEGER PRIMARY KEY,
        ${Bookings.Booking.COLUMN_TITLE} TEXT,
        ${Bookings.Booking.COLUMN_YEAR} INTEGER,
        ${Bookings.Booking.COLUMN_MONTH} INTEGER,
        ${Bookings.Booking.COLUMN_DAY} INTEGER,
        ${Bookings.Booking.COLUMN_START_HOUR} INTEGER,
        ${Bookings.Booking.COLUMN_START_MIN} INTEGER,
        ${Bookings.Booking.COLUMN_END_HOUR} INTEGER,
        ${Bookings.Booking.COLUMN_END_MIN} INTEGER,
        ${Bookings.Booking.COLUMN_ROOM} TEXT);
    """

const val SQL_CREATE_BOOKINGS_OFFLINE =
    """
    CREATE TABLE IF NOT EXISTS ${Bookings.BookingOffline.DB_TABLE} (
        ${Bookings.BookingOffline.COLUMN_ID} INTEGER PRIMARY KEY,
        ${Bookings.BookingOffline.COLUMN_TITLE} TEXT,
        ${Bookings.BookingOffline.COLUMN_YEAR} INTEGER,
        ${Bookings.BookingOffline.COLUMN_MONTH} INTEGER,
        ${Bookings.BookingOffline.COLUMN_DAY} INTEGER,
        ${Bookings.BookingOffline.COLUMN_START_HOUR} INTEGER,
        ${Bookings.BookingOffline.COLUMN_START_MIN} INTEGER,
        ${Bookings.BookingOffline.COLUMN_END_HOUR} INTEGER,
        ${Bookings.BookingOffline.COLUMN_END_MIN} INTEGER,
        ${Bookings.BookingOffline.COLUMN_ROOM} TEXT);
    """


private const val SQL_DELETE_BOOKINGS =
    "DROP TABLE IF EXISTS ${Bookings.Booking.DB_TABLE};"

private const val SQL_DELETE_BOOKINGS_OFFLINE =
    "DROP TABLE IF EXISTS ${Bookings.BookingOffline.DB_TABLE};"

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context,
        Bookings.DB_NAME,null,
        Bookings.DB_VERSION
    ) {

    var context: Context? = context

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL(SQL_CREATE_BOOKINGS)
        db.execSQL(SQL_CREATE_BOOKINGS_OFFLINE)
        context?.toast("Database was created")
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.i("ALEX", "Downgrade")
        db!!.execSQL(SQL_DELETE_BOOKINGS)
        db.execSQL(SQL_DELETE_BOOKINGS_OFFLINE)
        onCreate(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}