package com.example.contacts

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat


class AboutActivity : AppCompatActivity() {

    override fun onStart() {
        super.onStart()
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED) {

            val intent = Intent(this@AboutActivity, MainActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
    }
}