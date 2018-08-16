package com.matheusfroes.gamerguide.ui.intro

import agency.tango.materialintroscreen.MaterialIntroActivity
import agency.tango.materialintroscreen.SlideFragmentBuilder
import android.os.Bundle
import com.matheusfroes.gamerguide.R


class IntroActivity : MaterialIntroActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        addSlide(SlideFragmentBuilder()
                .backgroundColor(R.color.colorPrimary)
                .buttonsColor(R.color.colorPrimaryDark)
                .image(agency.tango.materialintroscreen.R.drawable.ic_next)
                .title("title 3")
                .description("Description 3")
                .build())
    }
}
