package com.personal.tmdb.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PreferencesEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {
    abstract fun getPreferencesDao(): PreferencesDao
    abstract fun getUserDao(): UserDao
}