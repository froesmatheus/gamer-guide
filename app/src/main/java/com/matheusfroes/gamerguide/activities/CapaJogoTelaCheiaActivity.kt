package com.matheusfroes.gamerguide.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Window
import com.matheusfroes.gamerguide.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_capa_jogo_tela_cheia.*


class CapaJogoTelaCheiaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_capa_jogo_tela_cheia)
        this.setFinishOnTouchOutside(true)

        intent ?: return

        val imagemCapa = intent.getStringExtra("url_imagem")

        Picasso
                .with(this)
                .load(imagemCapa.replace("t_thumb", "t_720p"))
                .fit()
                .into(photoView)
    }

}
