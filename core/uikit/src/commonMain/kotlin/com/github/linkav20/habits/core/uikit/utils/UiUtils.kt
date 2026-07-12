package com.github.linkav20.habits.core.uikit.utils

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.math.pow
import kotlin.math.roundToInt

fun formatTimeFromSec(seconds: Int): String {
    val secondsInMinute = 60
    val secondsInHour = secondsInMinute * 60
    val secondsInDay = secondsInHour * 24

    val days = seconds / secondsInDay
    val hours = (seconds % secondsInDay) / secondsInHour
    val minutes = (seconds % secondsInHour) / secondsInMinute
    val leftSeconds = (seconds % secondsInMinute)

    val hoursStr = if (hours < 10) "0$hours" else "$hours"
    val minutesStr = if (minutes < 10) "0$minutes" else "$minutes"
    val secondsStr = if (leftSeconds < 10) "0$leftSeconds" else "$leftSeconds"

    return if (minutes == 0) {
        "00:$secondsStr"
    } else if (hours == 0) {
        "$minutesStr:$secondsStr"
    } else if (days == 0) {
        "$hoursStr:$minutesStr:$secondsStr"
    } else {
        "$days:$hoursStr:$minutesStr:$secondsStr"
    }
}

fun Float.toText(numOfDec: Int): String {
    val integerDigits = this.toInt()
    val floatDigits = ((this - integerDigits) * 10f.pow(numOfDec)).roundToInt()
    return if (floatDigits == 0) {
        "$integerDigits"
    } else {
        "$integerDigits.$floatDigits"
    }
}

@Composable
fun Modifier.addClickable(onClick: (() -> Unit)?) =
    if (onClick != null) {
        this.clickable { onClick() }
    } else {
        this
    }
