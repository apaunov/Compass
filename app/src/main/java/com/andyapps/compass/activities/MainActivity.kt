package com.andyapps.compass.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.andyapps.compass.R
import com.andyapps.compass.views.CompassView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val compassView = findViewById<CompassView>(R.id.compass_view)
        compassView.bearing = 0f
    }
}
