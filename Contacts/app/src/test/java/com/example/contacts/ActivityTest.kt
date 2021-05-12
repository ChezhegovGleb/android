package com.example.contacts

import androidx.recyclerview.widget.RecyclerView
import androidx.test.filters.MediumTest
import androidx.test.rule.GrantPermissionRule
import junit.framework.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config


@MediumTest
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Config.OLDEST_SDK])

class ActivityTest {

    @get:Rule
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.READ_CONTACTS)

    private var activity: MainActivity? = null

    @Before
    @Throws(java.lang.Exception::class)
    fun setUp() {
        activity = Robolectric.buildActivity<MainActivity>(MainActivity::class.java)
            .create()
            .resume()
            .get()
    }


    @Test
    @Throws(Exception::class)
    fun activityNotNullTest() {
        assertNotNull(activity)
    }

    @Test
    fun searchNotNullTest() {
        val search: androidx.appcompat.widget.SearchView = activity!!.findViewById(R.id.search)
        assertNotNull(search)
    }

    @Test
    fun recyclerNotNullTest() {
        val recyclerView: RecyclerView = activity!!.findViewById(R.id.my_recycler_view)
        assertNotNull(recyclerView)
    }
}