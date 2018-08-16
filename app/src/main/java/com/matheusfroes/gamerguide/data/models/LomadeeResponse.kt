package com.matheusfroes.gamerguide.data.models

data class LomadeeResponse(val requestInfo: RequestInfo,
                           val pagination: Pagination,
                           val products: List<Product>)

data class RequestInfo(val status: String,
                       val message: String)

data class Pagination(val page: Long,
                      val size: Long,
                      val totalSize: Long,
                      val totalPage: Long)

data class Product(val id: Long,
                   val name: String,
                   val priceMin: Double,
                   val priceMax: Double,
                   val discount: Long,
                   val thumbnail: Thumbnail,
                   val link: String)

data class Thumbnail(val url: String,
                     val height: Long,
                     val width: Long,
                     val otherFormats: List<OtherFormat>)

data class OtherFormat(val url: String,
                       val height: Long,
                       val width: Long)

