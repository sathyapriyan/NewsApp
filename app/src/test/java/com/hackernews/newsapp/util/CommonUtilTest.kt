package com.hackernews.newsapp.util

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import javax.inject.Inject


@RunWith(JUnit4::class)
class CommonUtilTest @Inject constructor(
    @ApplicationContext val context: Context
) {

    @Test
    fun getTimeFromEpoch() {

        val currentTimeInMillis:Long = 1669972773
        val result = CommonUtil.convertEpochToActualTime(timeInMillis = currentTimeInMillis)
        assertEquals("02-12-2022 02:49 PM", result)

    }

}