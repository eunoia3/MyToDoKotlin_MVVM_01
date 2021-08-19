package com.eun.mytodokotlin_mvvm_01

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class Util {
    fun Long.toDateString(format: String, context: Context): String {
        val simpleDateFormat = SimpleDateFormat(format)
        return simpleDateFormat.format((Date()))
    }

}