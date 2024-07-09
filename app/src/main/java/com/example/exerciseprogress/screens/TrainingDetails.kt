package com.example.exerciseprogress.screens

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import com.example.exerciseprogress.data.ExerciseDatabase
import com.example.exerciseprogress.data.ExerciseOnTraining
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun TrainingDetailScreen(trainingId: Int, context: Context) {
    val db = remember { ExerciseDatabase.getDatabase(context) }
    val exerciseOnTrainingDao = remember { db.exerciseOnTrainingDao() }
    val exerciseDao = remember { db.exerciseDao() }

    val exercisesOnTraining: LiveData<List<ExerciseOnTraining>> = exerciseOnTrainingDao.getExercisesForTraining(trainingId)

    val exerciseList by exercisesOnTraining.observeAsState(emptyList())

    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "Szczegóły treningu:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn {
            items(exerciseList) { exercise ->
                var exerciseName by remember { mutableStateOf("") }

                LaunchedEffect(exercise.exercise_id) {
                    coroutineScope.launch {
                        val name = withContext(Dispatchers.IO) {
                            exerciseOnTrainingDao.getExerciseNameById(exercise.exercise_id)
                        }
                        exerciseName = name
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(text = "Ćwiczenie: $exerciseName", fontSize = 20.sp)
                        Text(text = "Powtórzenia: ${exercise.repetitions}", fontSize = 16.sp)
                        Text(text = "Serie: ${exercise.sets}", fontSize = 16.sp)
                        Text(text = "Waga: ${exercise.weight}", fontSize = 16.sp)
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    exerciseOnTrainingDao.deleteExerciseOnTraining(exercise)
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete exercise")
                    }
                }
            }
        }
    }
}