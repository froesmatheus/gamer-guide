package com.matheusfroes.gamerguide.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.matheusfroes.gamerguide.UserPreferences
import com.matheusfroes.gamerguide.appInjector
import javax.inject.Inject

open class BaseActivity : AppCompatActivity() {

    @Inject
    lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        appInjector.inject(this)
        setTheme(userPreferences.getCurrentAppTheme())
        super.onCreate(savedInstanceState)
    }
}