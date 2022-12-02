package com.hackernews.newsapp.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Html
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object CommonUtil {

    fun hasInternetConnection(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork

        if (activeNetwork != null) {

            val networkCapabilities = connectivityManager.getNetworkCapabilities(activeNetwork)

            return networkCapabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

        }

        return false

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertEpochToActualTime(timeInMillis: Long): String {

        return Instant.ofEpochSecond(timeInMillis)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
            .format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a"))
            .toString()

    }

    fun toastMessage(context: Context, message: String): Toast {

        return Toast.makeText(
            context,
            message,
            Toast.LENGTH_SHORT
        )

    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun htmlEntityToString(htmlEntityString: String): String {

        return Html
            .fromHtml(htmlEntityString, Html.FROM_HTML_MODE_COMPACT)
            .toString()

    }

}