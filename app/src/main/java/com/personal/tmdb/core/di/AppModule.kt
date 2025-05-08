package com.personal.tmdb.core.di

import android.content.ContentValues
import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import coil3.ImageLoader
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.ImageRequest
import com.personal.tmdb.BuildConfig
import com.personal.tmdb.core.data.local.AppDatabase
import com.personal.tmdb.core.data.remote.TmdbApi
import com.personal.tmdb.core.domain.util.AdditionalNavigationItem
import com.personal.tmdb.core.domain.util.C
import com.personal.tmdb.core.domain.util.CustomDns
import com.personal.tmdb.detail.data.models.RatedAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        val prepopulateCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                val defaultPreferences = ContentValues().apply {
                    put("language", "en")
                    put("showTitle", true)
                    put("showVoteAverage", true)
                    put("additionalNavigationItem", AdditionalNavigationItem.WATCHLIST.key)
                }
                db.insert("preferencesentity", SQLiteDatabase.CONFLICT_REPLACE, defaultPreferences)
            }
        }
        return Room.databaseBuilder(
            context = appContext,
            klass = AppDatabase::class.java,
            name = "app_database"
        ).fallbackToDestructiveMigration().addCallback(prepopulateCallback).build()
    }

    @Singleton
    @Provides
    fun providePreferencesDao(db: AppDatabase) = db.getPreferencesDao()

    @Singleton
    @Provides
    fun provideUserDao(db: AppDatabase) = db.getUserDao()

    @Singleton
    @Provides
    fun provideTmdbApi(): TmdbApi {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .dns(CustomDns())
            .build()

        val moshi = Moshi.Builder()
            .add(RatedAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
        return Retrofit.Builder()
            .baseUrl(C.TMDB_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(TmdbApi::class.java)
    }

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext appContext: Context): SharedPreferences = appContext.getSharedPreferences("localCache", Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideImageRequestBuilder(@ApplicationContext appContext: Context): ImageRequest.Builder = ImageRequest.Builder(appContext)

    @Singleton
    @Provides
    fun provideImageLoaderBuilder(@ApplicationContext appContext: Context): ImageLoader.Builder {
        val okHttpClient = OkHttpClient.Builder()
            .dns(CustomDns())
            .build()
        return ImageLoader.Builder(appContext).components { add(OkHttpNetworkFetcherFactory(callFactory = okHttpClient)) }
    }
}