package com.matheusfroes.gamerguide.activities

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by matheus_froes on 22/09/2017.
 */
open class BaseActivity : AppCompatActivity() {
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val theme = preferences.getInt("APP_THEME", R.style.AppTheme)
//        setTheme(theme)
    }
}