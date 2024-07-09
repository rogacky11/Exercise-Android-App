package com.example.exerciseprogress.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_table")
data class Training(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val training_date: String
)