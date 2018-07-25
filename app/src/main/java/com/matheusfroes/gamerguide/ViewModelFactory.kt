package com.matheusfroes.gamerguide

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.matheusfroes.gamerguide.di.annotation.AppScope
import javax.inject.Inject
import javax.inject.Provider

@AppScope
class ViewModelFactory @Inject constructor(
        private val creators: MutableMap<Class<out ViewModel>, Provider<ViewModel>>
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        var creator = creators[modelClass]

        if (creator == null) {
            creator = creators.entries.firstOrNull { (key, _) ->
                key.isAssignableFrom(modelClass)
            }?.value
        }

        if (creator == null) {
            throw IllegalArgumentException("Unknow model class $modelClass")
        }

        @Suppress("UNCHECKED_CAST")
        return creator.get() as T
    }

}