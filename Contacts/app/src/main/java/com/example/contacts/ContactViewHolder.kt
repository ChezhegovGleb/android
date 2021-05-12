package com.example.contacts

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.list_item.view.*

class ContactViewHolder(val root: View) : RecyclerView.ViewHolder(root) {
    val contactFirstNameText = root.contact_firstname
    val contactLastNameText  = root.contact_lastname
}