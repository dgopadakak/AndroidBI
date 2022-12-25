package com.example.androidbi.olympiad

data class Task(
    val nameOfTask: String,
    val hint: String,
    val num: Int,
    val numOfParticipants: String,
    val timeForSolve: String,
    val maxScore: Int,
    val isComplicated: Int,
    val condition: String
)
