package com.example.androidbi.olympiad.dbWithRoom

import androidx.room.TypeConverter
import com.example.androidbi.olympiad.Round
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class RoundOperatorConverter
{
    @TypeConverter
    fun fromGO(rounds: ArrayList<Round>): String
    {
        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()
        return gson.toJson(rounds)
    }

    @TypeConverter
    fun toGO(data: String): ArrayList<Round>
    {
        val gsonBuilder = GsonBuilder()
        val gson: Gson = gsonBuilder.create()
        return try {
            val type: Type = object : TypeToken<ArrayList<Round>>() {}.type
            gson.fromJson(data, type)
        } catch (e: Exception) {
            ArrayList()
        }
    }
}