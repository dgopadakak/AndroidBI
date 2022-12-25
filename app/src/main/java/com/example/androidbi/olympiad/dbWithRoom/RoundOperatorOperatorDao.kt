package com.example.androidbi.olympiad.dbWithRoom

import androidx.room.*
import com.example.androidbi.olympiad.RoundOperator

@Dao
interface RoundOperatorOperatorDao
{
    @Query("SELECT * FROM RoundOperator")
    fun getAll(): List<RoundOperator?>?

    @Query("SELECT * FROM RoundOperator WHERE id = :id")
    fun getById(id: Int): RoundOperator

    @Insert
    fun insert(go: RoundOperator?)

    @Delete
    fun delete(go: RoundOperator?)
}