package com.matheusfroes.gamerguide.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.view.MenuItem
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.db.FonteNoticiasDAO
import com.matheusfroes.gamerguide.ui.AppCompatPreferenceActivity
import org.jetbrains.anko.toast


@Suppress("DEPRECATION")
/**
 * A [PreferenceActivity] that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 *
 * See [Android Design: Settings](http://developer.android.com/design/patterns/settings.html)
 * for design guidelines and the [Settings API Guide](http://developer.android.com/guide/topics/ui/settings.html)
 * for more information on developing a Settings UI.
 */
class ConfiguracoesActivity : AppCompatPreferenceActivity() {
    private val preferences: SharedPreferences by lazy {
        getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE)
    }
    private val fonteNoticiasDAO: FonteNoticiasDAO by lazy {
        FonteNoticiasDAO(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val appTheme = preferences.getString("APP_THEME", "DEFAULT")
        val theme = if (appTheme == "DEFAULT") R.style.ConfiguracoesTheme else R.style.ConfiguracoesTheme_OLED
        setTheme(theme)
        super.onCreate(savedInstanceState)
        setupActionBar()
        addPreferencesFromResource(R.xml.pref_general)

        val switchHabilitarModoNoturno = findPreference("switchHabilitarModoNoturno")

        val preferenceScreen = this.preferenceScreen

        val preferenceCategory = preferenceScreen.getPreference(1) as PreferenceCategory

        val fonteNoticias = fonteNoticiasDAO.obterFonteNoticias()
        fonteNoticias.forEach { fonteNoticia ->
            val switchPreference = SwitchPreference(this)
            switchPreference.title = fonteNoticia.nome
            switchPreference.summary = fonteNoticia.website
            switchPreference.isChecked = fonteNoticia.ativado
            switchPreference.key = fonteNoticia.id.toString()

            switchPreference.setOnPreferenceChangeListener { preference, any ->
                val fonteAtivada = any as Boolean
                val fonteId = preference.key.toInt()
                fonteNoticiasDAO.alterarStatusFonteNoticia(fonteId, fonteAtivada)
                true
            }

            preferenceCategory.addPreference(switchPreference)
        }


        switchHabilitarModoNoturno.setOnPreferenceChangeListener { _, any ->
            val modoNoturnoHabilitado = any as Boolean

            if (modoNoturnoHabilitado) {
                preferences.edit().putString("APP_THEME", "MODO_NOTURNO").apply()
            } else {
                preferences.edit().putString("APP_THEME", "DEFAULT").apply()
            }

            toast(getString(R.string.reinicie_app_msg))
            true
        }

    }


    /**
     * Helper method to determine if the device has an extra-large screen. For
     * example, 10" tablets are extra-large.
     */
    private fun isXLargeTablet(context: Context): Boolean =
            context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE


    /**
     * Set up the [android.app.ActionBar], if the API is available.
     */
    private fun setupActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                this.finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * {@inheritDoc}
     */
    override fun onIsMultiPane(): Boolean = isXLargeTablet(this)

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    override fun isValidFragment(fragmentName: String): Boolean =
            PreferenceFragment::class.java.name == fragmentName
}