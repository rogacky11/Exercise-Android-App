package com.example.exerciseprogress.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_on_training_table",
    foreignKeys = [
        ForeignKey(
            entity = Training::class,
            parentColumns = ["id"],
            childColumns = ["training_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Exercise::class,
            parentColumns = ["id"],
            childColumns = ["exercise_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ExerciseOnTraining(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val training_id: Int?,
    val exercise_id: Int?,
    val repetitions: Int?,
    val sets: Int?,
    val weight: Float?
)
