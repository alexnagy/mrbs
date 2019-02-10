package com.example.anagy.mrbs.model

import android.util.Log
import org.json.JSONObject


class Booking(var title: String, var year: Int, var month: Int, var day: Int, var startHour: Int,
              var startMinute: Int, var endHour: Int, var endMinute: Int, var room: String) {

    var id: Int = -1

    constructor(_id: Int, title: String, year: Int, month: Int, day: Int, startHour: Int, startMinute: Int,
                endHour: Int, endMinute: Int, room: String) : this(title, year, month, day, startHour, startMinute,
        endHour, endMinute, room) {
        id = _id
    }

    override fun toString(): String {
        return title
    }

    fun toJSON(): JSONObject {
        val obj = JSONObject()
        obj.put("Id", id)
        obj.put("Title", title)
        obj.put("Day", day)
        obj.put("Month", month)
        obj.put("Year", year)
        obj.put("StartHour", startHour)
        obj.put("StartMin", startMinute)
        obj.put("EndHour", endHour)
        obj.put("EndMin", endMinute)
        obj.put("Room", room)

        return obj
    }

    fun fromJSON(obj: JSONObject): Booking {
        val id = obj.optInt("Id")
        val title = obj.optString("Title")
        val day = obj.optInt("Day")
        val month = obj.optInt("Month")
        val year = obj.optInt("Year")
        val startHour = obj.optInt("StartHour")
        val startMin = obj.optInt("StartMin")
        val endHour = obj.optInt("EndHour")
        val endMin = obj.optInt("EndMin")
        val room = obj.optString("Room")

        return Booking(id, title, year, month, day, startHour, startMin, endHour, endMin, room)
    }
}