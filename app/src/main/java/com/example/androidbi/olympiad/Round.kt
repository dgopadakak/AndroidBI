package com.example.androidbi.olympiad

data class Round(
    val name: String,
    var listOfTasks: ArrayList<Task> = ArrayList()
)
