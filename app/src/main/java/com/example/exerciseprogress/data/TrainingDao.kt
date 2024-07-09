package com.example.exerciseprogress.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TrainingDao {
    @Query("SELECT * FROM training_table ORDER BY id ASC")
    fun getAllTrainings(): LiveData<List<Training>>

    @Insert
     fun addTraining(training: Training): Long

    @Delete
     fun deleteTraining(training: Training)
}