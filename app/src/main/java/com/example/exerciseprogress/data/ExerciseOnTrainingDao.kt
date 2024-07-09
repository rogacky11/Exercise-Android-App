package com.example.exerciseprogress.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExerciseOnTrainingDao {
    @Insert
    fun addExerciseOnTraining(exerciseOnTraining: ExerciseOnTraining?): Long

    @Query("SELECT * FROM exercise_on_training_table WHERE training_id = :trainingId")
    fun getExercisesForTraining(trainingId: Int): LiveData<List<ExerciseOnTraining>>

//    @Query("DELETE FROM exercise_on_training_table WHERE exercise_id = :exerciseID")
    @Delete
    fun deleteExerciseOnTraining(exerciseOnTraining: ExerciseOnTraining?)

    @Query("SELECT exercise_name FROM exercise_table WHERE id = :exerciseId")
    fun getExerciseNameById(exerciseId: Int?): String
}

