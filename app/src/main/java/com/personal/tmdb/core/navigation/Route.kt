package com.personal.tmdb.core.navigation

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Home: Route

    @Serializable
    data object Search: Route

    @Serializable
    data object Profile: Route

    @Serializable
    data class Detail(val mediaType: String, val mediaId: Int): Route

    @Serializable
    data class Reviews(val mediaType: String, val mediaId: Int, val selectedReviewIndex: Int? = null): Route

    @Serializable
    data class Episodes(val mediaId: Int, val seasonNumber: Int): Route

    @Serializable
    data class Episode(val mediaId: Int, val seasonNumber: Int, val episodeNumber: Int): Route

    @Serializable
    data class Collection(val collectionId: Int): Route

    @Serializable
    data class Cast(val mediaName: String, val mediaType: String, val mediaId: Int, val seasonNumber: Int? = null, val episodeNumber: Int? = null): Route

    @Serializable
    data class Person(val personName: String, val personId: Int): Route

    @Serializable
    data class Images(val imageType: String, val imagesPath: String, val selectedImageIndex: Int? = null): Route

    @Serializable
    data object Settings: Route

    @Serializable
    data object Appearance: Route

    @Serializable
    data object Language: Route

    @Serializable
    data object Watchlist: Route

    @Serializable
    data object MyLists: Route

    @Serializable
    data class ListDetails(val listId: Int): Route

    @Serializable
    data object Favorite: Route

    @Serializable
    data object Lost: Route

    @Serializable
    data class AddToList(val mediaType: String, val mediaId: Int): Route

    @Serializable
    data object WelcomeBack: Route

    @Serializable
    data object DiscoverGraph: Route

    @Serializable
    data class Discover(val mediaType: String): Route

    @Serializable
    data object DiscoverFilters: Route

    @Serializable
    data class Genre(val mediaType: String, val genreId: Int): Route

    @Serializable
    data class ImagesPreview(
        val selectedIndex: Int,
        val images: List<String?>
    ): Route
}