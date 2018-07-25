package com.matheusfroes.gamerguide.di.modules

import android.content.Context
import com.matheusfroes.gamerguide.GamerGuideApplication
import com.matheusfroes.gamerguide.di.annotation.AppContext
import dagger.Module
import dagger.Provides


@Module(includes = [(ViewModelModule::class)])
class AppModule {

    @Provides
    @AppContext
    fun application(app: GamerGuideApplication): Context = app.applicationContext
}
