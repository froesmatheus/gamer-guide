package com.matheusfroes.gamerguide

import android.annotation.SuppressLint
import android.app.Activity
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import com.matheusfroes.gamerguide.di.Injector
import java.text.SimpleDateFormat
import java.util.*

fun Date.formatarData(formato: String): String =
        SimpleDateFormat(formato, Locale("pt", "BR")).format(this)

val Activity.app: GamerGuideApplication get() = application as GamerGuideApplication
val Fragment.app: GamerGuideApplication get() = activity.app

val Fragment.appInjector: Injector get() = app.injector

@SuppressLint("RestrictedApi")
fun BottomNavigationView.disableShiftMode() {
    val menuView = this.getChildAt(0) as BottomNavigationMenuView

    try {
        val shiftingMode = menuView.javaClass.getDeclaredField("mShiftingMode")
        shiftingMode.isAccessible = true
        shiftingMode.setBoolean(menuView, false)
        shiftingMode.isAccessible = false

        for (i in 0 until menuView.childCount) {
            val item = menuView.getChildAt(i) as BottomNavigationItemView
            item.setShiftingMode(false)
            item.setChecked(item.itemData.isChecked)
        }
    } catch (e: NoSuchFieldException) {
        e.printStackTrace()
    }
}