package com.example.anagy.mrbs.service

import android.os.AsyncTask.execute
import android.os.AsyncTask
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket


internal class InternetCheck(private val mConsumer: Consumer, private val host: String, private val port: Int) :
    AsyncTask<Void, Void, Boolean>() {
    interface Consumer {
        fun accept(internet: Boolean?)
    }

    init {
        execute()
    }

    override fun doInBackground(vararg voids: Void): Boolean? {
        return try {
            val sock = Socket()
            sock.connect(InetSocketAddress(host, port), 1500)
            sock.close()
            true
        } catch (e: IOException) {
            false
        }

    }

    override fun onPostExecute(internet: Boolean?) {
        mConsumer.accept(internet)
    }
}
