package com.hackernews.newsapp.util

import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@RunWith(JUnit4::class)
class CommonUtilTest {

    @Test
    fun getTimeFromEpoch() {

        val currentTimeInMillis:Long = 1669972773
        val result = CommonUtil.convertEpochToActualTime(timeInMillis = currentTimeInMillis)
        assertEquals("02-12-2022 02:49 PM", result)

    }

}