package com.personal.tmdb.core.domain.util

object TMDB {
    fun backdropW1280(path: String?) = C.TMDB_IMAGES_BASE_URL + C.BACKDROP_W1280 + path
    fun logoW500(path: String?) = C.TMDB_IMAGES_BASE_URL + C.LOGO_W500 + path
}