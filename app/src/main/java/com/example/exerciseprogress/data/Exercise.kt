package com.example.exerciseprogress.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_table")
 data class Exercise (
    @PrimaryKey(autoGenerate = true)
     val id: Int,
     val exercise_name: String
 )

