package com.example.notes.utils.extensions

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import com.example.notes.R
import com.example.notes.utils.enums.ThemeStatus
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun String.formatDate(): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val dateTime = LocalDateTime.parse(this)
    return dateTime.format(formatter)
}

fun String.themeEnum(): ThemeStatus {

    val enum: ThemeStatus

    when (this) {
        "light" -> enum = ThemeStatus.LIGHT
        "dark" -> enum = ThemeStatus.DARK
        else -> enum = ThemeStatus.SYSTEM
    }
    return enum
}

fun ThemeStatus.getThemeModeInt(): Int {

    val mode: Int

    when (this) {
        ThemeStatus.LIGHT -> mode = AppCompatDelegate.MODE_NIGHT_NO
        ThemeStatus.DARK -> mode = AppCompatDelegate.MODE_NIGHT_YES
        else -> mode = AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }
    return mode
}

fun ThemeStatus.getDrawableInt(): Int {

    val drawableInt: Int

    when (this) {
        ThemeStatus.LIGHT -> drawableInt = R.drawable.icon_sun_24
        ThemeStatus.DARK -> drawableInt = R.drawable.icon_night_24
        else -> drawableInt = R.drawable.icon_day_and_night_24
    }
    return drawableInt
}

fun ThemeStatus.getThemeString() : String {
    val themeString : String

    when(this) {
        ThemeStatus.LIGHT -> themeString = "light"
        ThemeStatus.DARK -> themeString = "dark"
        else-> themeString = "system"
    }

    return themeString
}