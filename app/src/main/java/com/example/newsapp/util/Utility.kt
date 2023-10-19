package com.example.newsapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
class Utility {
    fun convretTime(unixTimestamp: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        val date = sdf.parse(unixTimestamp) // Parse the input string
        val outputSdf = SimpleDateFormat("MMM dd, yyyy", Locale.US)
        outputSdf.timeZone = TimeZone.getTimeZone("Asia/Jakarta") // Set the time zone to WIB
        return outputSdf.format(date)
    }
}