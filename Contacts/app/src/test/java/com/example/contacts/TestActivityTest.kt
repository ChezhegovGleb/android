package com.example.contacts

import androidx.test.core.app.ActivityScenario
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config


@RunWith(AndroidJUnit4::class)
@Config(sdk = [Config.OLDEST_SDK])
class TestActivityTest {
    @Test
    @Throws(Exception::class)
    fun testActivitiesCanBeDeclaredInADependencyLibrary() {
        ActivityScenario.launch(MainActivity::class.java)
    }
}