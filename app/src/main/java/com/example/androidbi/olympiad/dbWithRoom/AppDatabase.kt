package com.example.androidbi.olympiad.dbWithRoom

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.androidbi.olympiad.RoundOperator

@Database(entities = [ RoundOperator::class ], version=5, exportSchema = false)
abstract class AppDatabase: RoomDatabase()
{
    public abstract fun groupOperatorDao(): RoundOperatorOperatorDao
}