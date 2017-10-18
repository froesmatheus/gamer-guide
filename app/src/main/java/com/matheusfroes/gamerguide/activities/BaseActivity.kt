package com.matheusfroes.gamerguide.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.gamerguide.R

/**
 * Created by matheusfroes on 18/10/2017.
 */
open class BaseActivity : AppCompatActivity() {
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val appTheme = preferences.getString("APP_THEME", "DEFAULT")
        val theme = if (appTheme == "DEFAULT") R.style.AppTheme_NoActionBar else R.style.AppTheme_OLED
        setTheme(theme)
        super.onCreate(savedInstanceState)

    }
}