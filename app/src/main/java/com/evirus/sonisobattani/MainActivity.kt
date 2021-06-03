package com.evirus.sonisobattani

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnPndaiTanaman: TextView = findViewById(R.id.PindaiTanaman)
        btnPndaiTanaman.setOnClickListener(this)

    }
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.PindaiTanaman -> {
                val Intent1 = Intent(this@MainActivity, PindaiTanaman::class.java)
                startActivity(Intent1)
            }
        }
    }
}