package com.bar.youday.helper

import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class NoteHelper {

    companion object {
        fun convertDate(date: Date): String {
            val newFormat = SimpleDateFormat("dd MMM yy hh:mm", Locale.ENGLISH)
            return newFormat.format(date)
        }
    }

}