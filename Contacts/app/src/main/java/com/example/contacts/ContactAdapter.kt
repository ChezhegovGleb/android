package com.example.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter(
    val contacts: List<Contact>,
    val onClick: (Contact) -> Unit) : RecyclerView.Adapter<ContactViewHolder>() {

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.contactFirstNameText.text =
            contacts[position].name
        holder.contactLastNameText.text =
            contacts[position].phoneNumber
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ContactViewHolder {
        val holder = ContactViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.list_item,
                parent,
                false
            )
        )
        holder.root.setOnClickListener {
            onClick(contacts[holder.adapterPosition])
        }
        return holder
    }
}