package com.matheusfroes.gamerguide.di

import javax.inject.Qualifier


@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class GamerGuide

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RssParser