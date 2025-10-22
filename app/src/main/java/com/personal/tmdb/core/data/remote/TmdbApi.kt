package com.personal.tmdb.core.data.remote

import com.personal.tmdb.BuildConfig
import com.personal.tmdb.auth.data.models.AccessTokenBody
import com.personal.tmdb.auth.data.models.AccessTokenDto
import com.personal.tmdb.auth.data.models.RedirectToBody
import com.personal.tmdb.auth.data.models.RequestTokenBody
import com.personal.tmdb.auth.data.models.RequestTokenDto
import com.personal.tmdb.auth.data.models.SessionDto
import com.personal.tmdb.auth.data.models.UserDto
import com.personal.tmdb.core.data.models.ListDetailsDto
import com.personal.tmdb.core.data.models.ListsResponseDto
import com.personal.tmdb.core.data.models.MediaResponseDto
import com.personal.tmdb.core.domain.models.CreateListRequest
import com.personal.tmdb.core.domain.models.LogoutRequestBody
import com.personal.tmdb.core.domain.models.MediaRequest
import com.personal.tmdb.core.domain.models.UpdateListMediaRequest
import com.personal.tmdb.core.domain.models.UpdateListDetailsRequest
import com.personal.tmdb.core.domain.util.LanguageCode
import com.personal.tmdb.detail.data.models.AccountStates
import com.personal.tmdb.detail.data.models.CollectionDto
import com.personal.tmdb.detail.data.models.Credits
import com.personal.tmdb.detail.data.models.EpisodeDetailsDto
import com.personal.tmdb.detail.data.models.Genres
import com.personal.tmdb.detail.data.models.ImagesDto
import com.personal.tmdb.detail.data.models.MediaDetailDto
import com.personal.tmdb.detail.data.models.PersonDto
import com.personal.tmdb.detail.data.models.Rated
import com.personal.tmdb.detail.data.models.Reviews
import com.personal.tmdb.detail.data.models.SeasonDto
import com.personal.tmdb.discover.data.models.CountryDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/trending/all/{time_window}?")
    suspend fun getTrending(
        @Path("time_window") timeWindow: String,
        @Query("language") language: String?,
        @Query("page") page: Int
    ): MediaResponseDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/{media_type}/{media_id}?")
    suspend fun getMovieTvDetail(
        @Path("media_type") mediaType: String,
        @Path("media_id") mediaId: Int,
        @Query("session_id") sessionId: String?,
        @Query("language") language: String?,
        @Query("append_to_response") appendToResponse: String?,
        @Query("include_image_language") includeImageLanguage: String?
    ): MediaDetailDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/{media_type}/{media_id}}/account_states?")
    suspend fun getAccountStates(
        @Path("media_type") mediaType: String,
        @Path("media_id") mediaId: Int,
        @Query("session_id") sessionId: String?
    ): AccountStates

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/search/{search_type}?")
    suspend fun searchFor(
        @Path("search_type") searchType: String,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean?,
        @Query("language") language: String?,
        @Query("page") page: Int
    ): MediaResponseDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/collection/{collectionId}?")
    suspend fun getCollection(
        @Path("collectionId") collectionId: Int,
        @Query("language") language: String?
    ): CollectionDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/{media_type}/popular?")
    suspend fun getPopular(
        @Path("media_type") mediaType: String,
        @Query("language") language: String?,
        @Query("page") page: Int
    ): MediaResponseDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/tv/{series_id}/season/{season_number}?")
    suspend fun getSeasonDetails(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Query("language") language: String?
    ): SeasonDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/{media_type}/{media_id}/{method}?")
    suspend fun getCredits(
        @Path("media_type") mediaType: String,
        @Path("media_id") seriesId: Int,
        @Path("method") method: String,
        @Query("language") language: String?
    ): Credits

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/tv/{series_id}/season/{season_number}/episode/{episode_number}/credits?")
    suspend fun getEpisodeCredits(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("language") language: String?
    ): Credits

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/person/{person_id}?")
    suspend fun getPerson(
        @Path("person_id") personId: Int,
        @Query("language") language: String?,
        @Query("append_to_response") appendToResponse: String?
    ): PersonDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @POST("4/auth/request_token")
    suspend fun createRequestToken(
        @Body redirectTo: RedirectToBody
    ): RequestTokenDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @POST("4/auth/access_token")
    suspend fun createAccessToken(
        @Body requestToken: RequestTokenBody
    ): AccessTokenDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @POST("3/authentication/session/convert/4")
    suspend fun createSession(
        @Body accessToken: AccessTokenBody
    ): SessionDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/account?")
    suspend fun getUserDetails(
        @Query("session_id") sessionId: String
    ): UserDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/tv/{series_id}/season/{season_number}/episode/{episode_number}?")
    suspend fun getEpisodeDetails(
        @Path("series_id") seriesId: Int,
        @Path("season_number") seasonNumber: Int,
        @Path("episode_number") episodeNumber: Int,
        @Query("language") language: String?,
        @Query("append_to_response") appendToResponse: String?,
        @Query("include_image_language") includeImageLanguage: String?
    ): EpisodeDetailsDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/{path}?")
    suspend fun getImages(
        @Path("path") path: String,
        @Query("language") language: String?,
        @Query("include_image_language") includeImageLanguage: String?
    ): ImagesDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("/3/{media_type}/{media_id}/reviews?")
    suspend fun getReviews(
        @Path("media_type") mediaType: String,
        @Path("media_id") mediaId: Int,
        @Query("page") page: Int,
        @Query("language") language: String?
    ): Reviews

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/movie/upcoming?")
    suspend fun getUpcomingMovies(
        @Query("language") language: String?,
        @Query("page") page: Int,
        @Query("region") region: String
    ): MediaResponseDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/genre/{media_type}/list?")
    suspend fun getGenres(
        @Path("media_type") mediaType: String,
        @Query("language") language: String?
    ): Genres

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("4/account/{account_object_id}/{media_type}/watchlist?")
    suspend fun getWatchlist(
        @Path("account_object_id") accountObjectId: String,
        @Path("media_type") mediaType: String,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int,
        @Query("language") language: String?
    ): MediaResponseDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @POST("3/account/{account_id}/watchlist?")
    suspend fun updateWatchlistItem(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Body request: MediaRequest
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("4/account/{account_object_id}/lists?")
    suspend fun getLists(
        @Path("account_object_id") accountObjectId: String,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int
    ): ListsResponseDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("4/list/{list_id}?")
    suspend fun getListDetails(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int,
        @Query("language") language: String?
    ): ListDetailsDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @PUT("4/list/{list_id}")
    suspend fun updateListDetails(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String,
        @Body request: UpdateListDetailsRequest
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @HTTP(method = "DELETE", path = "4/list/{list_id}/items?", hasBody = true)
    suspend fun deleteListItems(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String,
        @Body request: UpdateListMediaRequest
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @DELETE("4/list/{list_id}")
    suspend fun deleteList(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @POST("4/list")
    suspend fun createList(
        @Query("session_id") sessionId: String,
        @Body request: CreateListRequest
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @POST("4/list/{list_id}/items?")
    suspend fun addItemsToList(
        @Path("list_id") listId: Int,
        @Query("session_id") sessionId: String,
        @Body request: UpdateListMediaRequest
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("4/account/{account_object_id}/{media_type}/recommendations?")
    suspend fun getRecommendations(
        @Path("account_object_id") accountObjectId: String,
        @Path("media_type") mediaType: String,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int,
        @Query("language") language: String?
    ): MediaResponseDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("4/account/{account_object_id}/{media_type}/favorites?")
    suspend fun getFavorites(
        @Path("account_object_id") accountObjectId: String,
        @Path("media_type") mediaType: String,
        @Query("session_id") sessionId: String,
        @Query("page") page: Int,
        @Query("language") language: String?
    ): MediaResponseDto

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @POST("3/account/{account_id}/favorite?")
    suspend fun updateFavoriteItem(
        @Path("account_id") accountId: Int,
        @Query("session_id") sessionId: String,
        @Body request: MediaRequest
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @HTTP(method = "DELETE", path = "3/authentication/session", hasBody = true)
    suspend fun removeAccessToken(
        @Body request: LogoutRequestBody
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @HTTP(method = "DELETE", path = "4/auth/access_token", hasBody = true)
    suspend fun removeSessionId(
        @Body request: LogoutRequestBody
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @POST("3/{media_type}/{media_id}}/rating?")
    suspend fun addRating(
        @Path("media_type") mediaType: String,
        @Path("media_id") mediaId: Int,
        @Query("session_id") sessionId: String,
        @Body request: Rated
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @DELETE("3/{media_type}/{media_id}}/rating?")
    suspend fun removeRating(
        @Path("media_type") mediaType: String,
        @Path("media_id") mediaId: Int,
        @Query("session_id") sessionId: String
    )

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/configuration/primary_translations")
    suspend fun getPrimaryTranslations(): List<LanguageCode>

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/configuration/countries")
    suspend fun getCountries(): List<CountryDto>

    @Headers("Authorization: Bearer ${BuildConfig.TMDB_API_KEY}")
    @GET("3/discover/{media_type}?")
    suspend fun discoverMedia(
        @Path("media_type") mediaType: String,
        @Query("language") language: String?,
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean,
        @Query("first_air_date_year") airDateYear: String,
        @Query("primary_release_year") releaseDateYear: String,
        @Query("first_air_date.gte") fromAirDate: String,
        @Query("primary_release_date.gte") fromReleaseDate: String,
        @Query("first_air_date.lte") toAirDate: String,
        @Query("primary_release_date.lte") toReleaseDate: String,
        @Query("sort_by") sortBy: String,
        @Query("vote_average.gte") fromRating: Float,
        @Query("vote_average.lte") toRating: Float,
        @Query("vote_count.gte") minRatingCount: Float,
        @Query("with_genres") withGenre: String,
        @Query("with_origin_country") withOriginCountry: String,
        @Query("with_original_language") withOriginalLanguage: String,
        @Query("with_runtime.gte") fromRuntime: Int,
        @Query("with_runtime.lte") toRuntime: Int,
    ): MediaResponseDto
}