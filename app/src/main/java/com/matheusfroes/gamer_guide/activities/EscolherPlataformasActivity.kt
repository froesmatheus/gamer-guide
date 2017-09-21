package com.matheusfroes.gamer_guide.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.matheusfroes.gamer_guide.R
import kotlinx.android.synthetic.main.activity_escolher_plataformas.*

class EscolherPlataformasActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_escolher_plataformas)

        btnContinuar.setOnClickListener { startActivity(Intent(this, TelaPrincipalActivity::class.java)) }
    }

    fun onImageClick(view: View) {
        when (view.id) {
            R.id.cvXbox360 -> {
                if (ivXbox360.tag == "colorido") {
                    ivXbox360.setImageResource(R.drawable.xbox_360_cinza)
                    ivXbox360.tag = "cinza"
                } else {
                    ivXbox360.setImageResource(R.drawable.xbox_360)
                    ivXbox360.tag = "colorido"
                }

            }
            R.id.cvXboxOne -> {
                if (ivXboxOne.tag == "colorido") {
                    ivXboxOne.setImageResource(R.drawable.xbox_one_cinza)
                    ivXboxOne.tag = "cinza"
                } else {
                    ivXboxOne.setImageResource(R.drawable.xbox_one)
                    ivXboxOne.tag = "colorido"
                }
            }
            R.id.cvPs3 -> {
                if (ivPs3.tag == "colorido") {
                    ivPs3.setImageResource(R.drawable.playstation_3_cinza)
                    ivPs3.tag = "cinza"
                } else {
                    ivPs3.setImageResource(R.drawable.playstation_3)
                    ivPs3.tag = "colorido"
                }
            }
            R.id.cvPs4 -> {
                if (ivPs4.tag == "colorido") {
                    ivPs4.setImageResource(R.drawable.playstation_4_cinza)
                    ivPs4.tag = "cinza"
                } else {
                    ivPs4.setImageResource(R.drawable.playstation_4)
                    ivPs4.tag = "colorido"
                }
            }
        }
    }
}
