package com.example.contacts

import android.app.Activity
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK])
class AboutActivityTest {
    @Test
    fun testActivityFound() {
        val activity: Activity = Robolectric.buildActivity(
            AboutActivity::class.java
        ).create().get()
        assertNotNull(activity)
    }
}