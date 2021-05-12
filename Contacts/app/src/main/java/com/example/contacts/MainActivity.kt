package com.example.contacts


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.list_item.view.*
import java.lang.Math.min


class MainActivity : AppCompatActivity(), androidx.appcompat.widget.SearchView.OnQueryTextListener {

    lateinit var myContacts: List<Contact>

    fun Context.fetchAllContacts(): List<Contact> {
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
            .use { cursor ->
                if (cursor == null) return emptyList()
                val builder = ArrayList<Contact>()
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) ?: "N/A"
                    var phoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ?: "N/A"

                    var newNumber: String = ""
                    for (i in phoneNumber.indices) {
                        if (phoneNumber[i].isDigit() || phoneNumber[i] == '+') {
                            newNumber += phoneNumber[i]
                        }
                    }

                    phoneNumber = newNumber

                    builder.add(Contact(name, phoneNumber))
                }
                return builder
            }
    }

    val myRequestId: Int = 0
    var flag: Boolean = false

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {

        when (requestCode) {
            myRequestId -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    flag = true
                    printContacts(fetchAllContacts())
                    return
                } else {
                    val intent = Intent(this@MainActivity, AboutActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                return
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this@MainActivity, // Контекст
                arrayOf(Manifest.permission.READ_CONTACTS), // Что спрашиваем
                myRequestId)

            if (!flag) {
                return
            }

        }

        myContacts = fetchAllContacts()
        printContacts(myContacts)

        var searchView = search
        searchView.setOnQueryTextListener(this@MainActivity)

    }

    fun printContacts(Contacts: List<Contact>) {
        val viewManager = LinearLayoutManager(this)
        var sym = "s"
        if (Contacts.size == 1) {
            sym = ""
        }
        Toast.makeText(this,
                "Found ${Contacts.size} contact$sym",
                Toast.LENGTH_SHORT)
            .show()

        findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            layoutManager = viewManager
            adapter = ContactAdapter(Contacts) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:${it.phoneNumber}")
                startActivity(intent)
            }
        }
    }


    override fun onQueryTextChange(newText: String?): Boolean {
        printContacts(myContacts.search(newText))
        return false
    }

    override fun onQueryTextSubmit(query: String?) : Boolean {
        printContacts(myContacts.search(query))
        return false
    }

    lateinit var queryList: java.util.ArrayList<Contact>
}

fun List<Contact>.search(query: String?) : List<Contact> {
    val myContacts = this
    val queryList = mutableListOf<Contact>()
    var count = 0
    val querySize = query!!.length

    for (i in myContacts.indices) {
        val currContact = myContacts[i]
        val minNameSize = min(querySize, myContacts[i].name.length)
        val minPhoneSize = min(querySize, myContacts[i].phoneNumber.length)
        if (minNameSize >= querySize && currContact.name.contains(query, true)) {
            queryList.add(myContacts[i])
            count++
        } else if (minPhoneSize >= querySize && currContact.phoneNumber.substring(0, querySize).equals(query)) {
            queryList.add(myContacts[i])
            count++
        }
    }

    return queryList
}
