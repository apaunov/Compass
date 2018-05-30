package com.andyapps.compass.activities

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Switch
import com.andyapps.compass.R
import com.andyapps.compass.views.CompassView
import java.util.*

class MainActivity : AppCompatActivity() {

    private var check = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val compassView = findViewById<CompassView>(R.id.compass_view)
        compassView.bearing = 0f

        val localSwitch = findViewById<Switch>(R.id.local_switch)

        localSwitch.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                Log.i("MainActivity", "isChecked true")
                setLocale(Locale.CANADA_FRENCH.language)
            } else {
                Log.i("MainActivity", "isChecked false")
                setLocale(Locale.CANADA.language)
            }
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)

        Log.i("MainActivity", "Attach Base Context")
    }

    private fun setLocale(language: String) {
        Log.i("MainActivity", language)
        val local = Locale(language)
        val res = resources
        val displayMetrics = res.displayMetrics
        val config = res.configuration

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.locales = LocaleList(local)
        } else {
            config.locale = local
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            applicationContext.createConfigurationContext(config)
        } else {
            res.updateConfiguration(config, displayMetrics)
        }

        if (check) {
            check = false
            recreate()
        }
    }
}
