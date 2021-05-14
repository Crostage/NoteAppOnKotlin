package com.bar.youday.helper

import java.text.SimpleDateFormat
import java.util.*

class NoteHelper {

    companion object {
        fun convertDate(date: Date): String {
            val newFormat = SimpleDateFormat("dd MMM yy hh:mm", Locale.ENGLISH)
            return newFormat.format(date)
        }
    }

}