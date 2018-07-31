package com.matheusfroes.gamerguide

sealed class Result<D> {

    class InProgress<D> : Result<D>()

    data class Complete<D>(val data: D) : Result<D>()

    data class Error<D>(val error: Throwable? = null) : Result<D>()

}
