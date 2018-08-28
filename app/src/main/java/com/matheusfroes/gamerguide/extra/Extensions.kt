package com.matheusfroes.gamerguide.extra

import android.annotation.SuppressLint
import android.app.Activity
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.annotation.StringRes
import android.support.design.internal.BottomNavigationItemView
import android.support.design.internal.BottomNavigationMenuView
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.matheusfroes.gamerguide.GamerGuideApplication
import com.matheusfroes.gamerguide.di.Injector
import com.matheusfroes.gamerguide.network.uiContext
import kotlinx.coroutines.experimental.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.experimental.CoroutineContext

fun Date.formatarData(formato: String): String =
        SimpleDateFormat(formato, Locale("pt", "BR")).format(this)

val Activity.app: GamerGuideApplication get() = application as GamerGuideApplication
val Fragment.app: GamerGuideApplication get() = requireActivity().app

val Fragment.appCompatActivity: AppCompatActivity get() = activity as AppCompatActivity
val Fragment.appInjector: Injector get() = app.injector
val Activity.appInjector: Injector get() = app.injector

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

/** Convenience for callbacks/listeners whose return value indicates an event was consumed. */
inline fun consume(f: () -> Unit): Boolean {
    f()
    return true
}

/**
 * Allows calls like
 *
 * `supportFragmentManager.inTransaction { add(...) }`
 */
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

/**
 * For Fragments, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> Fragment.viewModelProvider(
        provider: ViewModelProvider.Factory
) =
        ViewModelProviders.of(this, provider).get(VM::class.java)

/**
 * Like [Fragment.viewModelProvider] for Fragments that want a [ViewModel] scoped to the Activity.
 */
inline fun <reified VM : ViewModel> Fragment.activityViewModelProvider(
        provider: ViewModelProvider.Factory
) =
        ViewModelProviders.of(requireActivity(), provider).get(VM::class.java)

/**
 * For Actvities, allows declarations like
 * ```
 * val myViewModel = viewModelProvider(myViewModelFactory)
 * ```
 */
inline fun <reified VM : ViewModel> FragmentActivity.viewModelProvider(
        provider: ViewModelProvider.Factory
) =
        ViewModelProviders.of(this, provider).get(VM::class.java)

fun Fragment.toast(@StringRes text: Int) {
    Toast.makeText(view?.context, text, Toast.LENGTH_LONG).show()
}

fun Fragment.snack(@StringRes text: Int) {
    view?.let { Snackbar.make(it, getString(text), Snackbar.LENGTH_LONG).show() }
}

fun FragmentActivity.snack(text: String) {
    Snackbar.make(window.decorView.rootView, text, Snackbar.LENGTH_LONG).show()
}

fun FragmentActivity.snack(@StringRes text: Int) {
    Snackbar.make(window.decorView.rootView, getString(text), Snackbar.LENGTH_LONG).show()
}

fun uiThread(block: suspend CoroutineScope.() -> Unit): Job {
    return launch(uiContext) { block() }
}

fun Context.toast(message: CharSequence): Toast = Toast
        .makeText(this, message, Toast.LENGTH_SHORT)
        .apply {
            show()
        }

suspend fun <A, B> Collection<A>.parallelMap(
        context: CoroutineContext = DefaultDispatcher,
        block: suspend (A) -> B
): Collection<B> {
    return map {
        // Use async to start a coroutine for each item
        async(context) {
            block(it)
        }
    }.map {
        // We now have a map of Deferred<T> so we await() each
        it.await()
    }
}