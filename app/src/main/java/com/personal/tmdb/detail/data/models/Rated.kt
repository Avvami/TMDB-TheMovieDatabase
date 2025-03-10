package com.personal.tmdb.detail.data.models

sealed class Rated {
    data class Value(val value: Int): Rated()
    data object NotRated: Rated()
}