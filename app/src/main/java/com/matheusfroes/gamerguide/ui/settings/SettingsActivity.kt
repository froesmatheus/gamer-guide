package com.matheusfroes.gamerguide.ui.settings

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.preference.PreferenceActivity
import android.preference.PreferenceCategory
import android.preference.PreferenceFragment
import android.preference.SwitchPreference
import android.view.MenuItem
import com.matheusfroes.gamerguide.R
import com.matheusfroes.gamerguide.data.model.AppTheme
import com.matheusfroes.gamerguide.data.source.UserPreferences
import com.matheusfroes.gamerguide.data.source.local.NewsLocalSource
import com.matheusfroes.gamerguide.extra.appInjector
import com.matheusfroes.gamerguide.extra.toast
import com.matheusfroes.gamerguide.ui.AppCompatPreferenceActivity
import javax.inject.Inject


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
class SettingsActivity : AppCompatPreferenceActivity() {
    @Inject
    lateinit var userPreferences: UserPreferences

    @Inject
    lateinit var newsLocalSource: NewsLocalSource

    override fun onCreate(savedInstanceState: Bundle?) {
        appInjector.inject(this)
        setTheme(userPreferences.getSettingsScreenTheme())
        super.onCreate(savedInstanceState)
        setupActionBar()
        addPreferencesFromResource(R.xml.pref_general)

        val switchHabilitarModoNoturno = findPreference("switchHabilitarModoNoturno")

        val preferenceScreen = this.preferenceScreen

        val preferenceCategory = preferenceScreen.getPreference(1) as PreferenceCategory

        val newsSources = newsLocalSource.getNewsSources()
        newsSources.forEach { newsSource ->
            val switchPreference = SwitchPreference(this)
            switchPreference.title = newsSource.name
            switchPreference.summary = newsSource.website
            switchPreference.isChecked = newsSource.enabled
            switchPreference.key = newsSource.id.toString()

            switchPreference.setOnPreferenceChangeListener { preference, any ->
                val fonteAtivada = any as Boolean
                val fonteId = preference.key.toInt()
                newsLocalSource.updateNewsSourceStatus(fonteAtivada, fonteId)
//                fonteNoticiasDAO.alterarStatusFonteNoticia(fonteId, fonteAtivada)
                true
            }

            preferenceCategory.addPreference(switchPreference)
        }


        switchHabilitarModoNoturno.setOnPreferenceChangeListener { _, any ->
            val modoNoturnoHabilitado = any as Boolean

            if (modoNoturnoHabilitado) {
                userPreferences.currentAppTheme = AppTheme.PURE_BLACK
            } else {
                userPreferences.currentAppTheme = AppTheme.DEFAULT
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