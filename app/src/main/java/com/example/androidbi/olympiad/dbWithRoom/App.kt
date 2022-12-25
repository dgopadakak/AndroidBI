package com.example.androidbi.olympiad.dbWithRoom

import android.app.Application
import androidx.room.Room.databaseBuilder


class App : Application() {
    var database: AppDatabase? = null
        private set

    override fun onCreate() {
        super.onCreate()
        instance = this
        database = databaseBuilder(this, AppDatabase::class.java, "database")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
    }

    companion object {
        var instance: App? = null
    }
}