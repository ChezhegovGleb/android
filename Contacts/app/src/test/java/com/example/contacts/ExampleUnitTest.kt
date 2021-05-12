package com.example.contacts

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    @Test
    fun searchContactSimpleTest() {
        assertEquals(contacts.search("aaa"), listOf(CONTACT_AAA_999))
    }

    @Test
    fun notFoundContactTest() {
        assertEquals(contacts.search("d"), listOf<Contact>())
    }

    @Test
    fun searchBigLetterTest() {
        assertEquals(contacts.search("BBB"), listOf(CONTACT_BBB_1488))
    }

    @Test
    fun searchSubstringNameTest() {
        assertEquals(contacts.search("aca"), listOf(CONTACT_ABACABA_1, CONTACT_ABACABA_2))
    }

    @Test
    fun searchPhoneNumberSimpleTest() {
        assertEquals(contacts.search("88005553535"), listOf(CONTACT_ABACABA_1))
    }


    companion object {
        private val CONTACT_AAA_999 = Contact("aaa", "999")
        private val CONTACT_BBB_1488 = Contact("bbb", "1488")
        private val CONTACT_ABACABA_1 = Contact("AbAcAbA", "88005553535")
        private val CONTACT_ABACABA_2 = Contact("abacaba", "1489")

        private val contacts = listOf(CONTACT_AAA_999, CONTACT_BBB_1488, CONTACT_ABACABA_1, CONTACT_ABACABA_2)
    }
}
