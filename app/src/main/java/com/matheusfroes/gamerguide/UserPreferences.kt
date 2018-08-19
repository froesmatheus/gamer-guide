package com.matheusfroes.gamerguide

import android.support.annotation.StyleRes
import com.chibatching.kotpref.KotprefModel
import com.chibatching.kotpref.enumpref.enumValuePref
import com.matheusfroes.gamerguide.data.model.AppTheme
import javax.inject.Inject

class UserPreferences @Inject constructor() : KotprefModel() {

    companion object {
        const val CURRENT_APP_THEME = "com.matheusfroes.gamerguide.current_app_theme"
    }

    var currentAppTheme by enumValuePref(AppTheme.DEFAULT, key = CURRENT_APP_THEME)

    @StyleRes
    fun getCurrentAppTheme(): Int {
        return when (currentAppTheme) {
            AppTheme.DEFAULT -> R.style.AppTheme_NoActionBar
            AppTheme.PURE_BLACK -> R.style.AppTheme_OLED
        }
    }

    @StyleRes
    fun getGameDetailsScreenTheme(): Int {
        return when (currentAppTheme) {
            AppTheme.DEFAULT -> R.style.AppTheme_DetalhesJogo
            AppTheme.PURE_BLACK -> R.style.AppTheme_DetalhesJogo_OLED
        }
    }

    @StyleRes
    fun getSettingsScreenTheme(): Int {
        return when (currentAppTheme) {
            AppTheme.DEFAULT -> R.style.ConfiguracoesTheme
            AppTheme.PURE_BLACK -> R.style.ConfiguracoesTheme_OLED
        }
    }

}