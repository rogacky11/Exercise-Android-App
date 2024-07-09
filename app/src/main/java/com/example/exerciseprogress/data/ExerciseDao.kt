package com.example.exerciseprogress.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExerciseDao {

    @Query("SELECT * FROM exercise_table ORDER BY id ASC")
    fun getExercise(): LiveData<List<Exercise>>

    @Insert
    fun addExercise(exercise: Exercise): Long

    @Delete
    fun deleteExercise(exercise: Exercise)
}