package com.matheusfroes.gamerguide.models

/**
 * Created by matheus_froes on 20/09/2017.
 */

data class Noticia(val titulo: String, val imagem: String, val url: String, val dataPublicacao: Long, val website: String) {
    override fun equals(other: Any?): Boolean {
        return if (other !is Noticia) {
            false
        } else {
            other.titulo == this.titulo
        }
    }

    override fun hashCode(): Int {
        var result = titulo.hashCode()
        result = 31 * result + imagem.hashCode()
        result = 31 * result + url.hashCode()
        result = 31 * result + dataPublicacao.hashCode()
        result = 31 * result + website.hashCode()
        return result
    }
}
