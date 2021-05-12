package com.example.contacts

import android.app.Activity
import android.content.ContentResolver
import android.provider.ContactsContract
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.rule.GrantPermissionRule
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.hamcrest.core.IsEqual.equalTo
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config
import org.robolectric.fakes.RoboCursor
import org.robolectric.shadows.ShadowToast


@Config(sdk = [Config.OLDEST_SDK])
@RunWith(RobolectricTestRunner::class)
class MainActivityTest {

    @get:Rule
    val permissionRule: GrantPermissionRule =
        GrantPermissionRule.grant(android.Manifest.permission.READ_CONTACTS)

    @Before
    fun setUp() {
        contactsRoboCursor = RoboCursor().apply {
            setColumnNames(CONTACTS_COLUMNS)
            setResults(
                contacts.map {
                    arrayOf(it.name, it.phoneNumber)
                }.toTypedArray()
            )
        }

        contentResolver = mock {
            on {
                query(
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull(),
                    anyOrNull()
                )
            } doReturn contactsRoboCursor
        }

        activityController = Robolectric.buildActivity(MainActivity::class.java)
        activity = activityController.get()

        shadowOf(activity.contentResolver).setCursor(contactsRoboCursor)
    }

    @After
    fun cleanUp() {
        activityController.stop()
    }


    @Test
    fun validateContactsAfterStartActivity() {
        activity = activityController.create().get()

        checkRecyclerViewState(contacts)
        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo("Found 4 contacts"))
    }

    @Test
    fun searchContact_AAA_byName() {
        activity = activityController.create().get()

        setSearchText("aaa")
        checkRecyclerViewState(CONTACT_AAA_900, CONTACT_AAA_901, CONTACT_aaa_902)
        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo("Found 3 contacts"))
    }

    @Test
    fun searchContact_BoB_byPartOfName() {
        activity = activityController.create().get()

        setSearchText("b")
        checkRecyclerViewState(CONTACT_BoB_900)
        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo("Found 1 contact"))
    }

    @Test
    fun searchContactByNumber() {
        activity = activityController.create().get()

        setSearchText("900")
        checkRecyclerViewState(CONTACT_AAA_900, CONTACT_BoB_900)
        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo("Found 2 contacts"))
    }

    @Test
    fun searchUndefinedContact() {
        activity = activityController.create().get()

        setSearchText("zzz")
        checkRecyclerViewState()
        assertThat(ShadowToast.getTextOfLatestToast().toString(), equalTo("Found 0 contacts"))
        //ShadowToast.showedToast("Found 1 contacts")
    }

    private fun setSearchText(text: String) {
        val inputField: SearchView =
            activity.findViewById<SearchView?>(R.id.search)
                .checkThatNotNull()
        inputField.setQuery(text, true)
    }


    private fun checkRecyclerViewState(vararg expectedContacts: Contact) =
        checkRecyclerViewState(expectedContacts.toList())

    private fun checkRecyclerViewState(expectedContacts: List<Contact>) {
        val recyclerView: RecyclerView = activity.findViewById<RecyclerView?>(R.id.my_recycler_view)
            .checkThatNotNull()
        val adapter: ContactAdapter = recyclerView.adapter
            .checkThatNotNull()
            .checkType()

        assertEquals(adapter.contacts, expectedContacts)
    }


    companion object {
        private val CONTACTS_COLUMNS = listOf(
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        private val CONTACT_AAA_900 = Contact("AAA", "900")
        private val CONTACT_AAA_901 = Contact("AAA", "901")
        private val CONTACT_aaa_902 = Contact("aaa", "902")
        private val CONTACT_BoB_900 = Contact("BoB", "900")

        private val contacts: List<Contact> = listOf(
            CONTACT_AAA_900,
            CONTACT_AAA_901,
            CONTACT_aaa_902,
            CONTACT_BoB_900
        ).sortedBy { it.name.toLowerCase() }

        private lateinit var activity: Activity
        private lateinit var activityController: ActivityController<MainActivity>

        private lateinit var contactsRoboCursor: RoboCursor
        private lateinit var contentResolver: ContentResolver
    }

    fun <T> T?.checkThatNotNull(): T {
        assertNotNull(this)
        return this!!
    }

    inline fun <reified T> Any.checkType(): T {
        assertTrue(this is T)
        return this as T
    }
}
