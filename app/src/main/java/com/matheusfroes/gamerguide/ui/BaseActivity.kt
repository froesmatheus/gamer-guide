package com.matheusfroes.gamerguide.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import com.matheusfroes.gamerguide.R
import dagger.android.support.DaggerAppCompatActivity

open class BaseActivity : DaggerAppCompatActivity() {
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