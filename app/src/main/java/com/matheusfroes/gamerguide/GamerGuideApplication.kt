package com.matheusfroes.gamerguide

import android.app.Application
import android.arch.persistence.room.Room
import com.matheusfroes.gamerguide.data.GamerGuideDatabase
import com.matheusfroes.gamerguide.network.ApiService
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.eagerSingleton
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GamerGuideApplication : Application(), KodeinAware {

    override val kodein: Kodein = Kodein.lazy {
        bind<GamerGuideDatabase>() with eagerSingleton {
            Room.databaseBuilder(this@GamerGuideApplication, GamerGuideDatabase::class.java, "gamerguide-db")
                    .build()
        }

        bind<Retrofit>() with singleton {
            Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(ApiService.URL_BASE)
                    .build()
        }
    }
}