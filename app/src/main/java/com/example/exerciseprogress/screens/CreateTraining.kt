package com.example.exerciseprogress.screens


import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.exerciseprogress.data.Exercise
import com.example.exerciseprogress.data.ExerciseDatabase
import com.example.exerciseprogress.data.ExerciseOnTraining
import com.example.exerciseprogress.data.Training
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext




@SuppressLint("RememberReturnType", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExerciseScreen(
    navigationController: NavController, context: Context
) {
    val db = remember { ExerciseDatabase.getDatabase(context) }
    val exerciseOnTrainingDao = remember { db.exerciseOnTrainingDao() }

    val trainingDao = remember { db.trainingDao() }
    val trainings: LiveData<List<Training>> = trainingDao.getAllTrainings()
    val trainingList by trainings.observeAsState(emptyList())
    val currentTraining = trainingList.lastOrNull()
    val currentTrainingId = currentTraining?.id ?: 0


//    val exercises by viewModel.exercises.observeAsState(emptyList())
    val exerciseDao = remember { db.exerciseDao() }
    val exercises: LiveData<List<Exercise>> = exerciseDao.getExercise()
    val exerciseList by exercises.observeAsState(emptyList())



    var selectedExerciseName by remember { mutableStateOf("") }
    var selectedExerciseID by remember { mutableStateOf<Int?>(null) }
    var repetitions by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()


    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(300.dp))
        Text(text = "Select Exercise")

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selectedExerciseName,
                onValueChange = { },
                readOnly = true,
                label = { Text("Exercise") },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.ArrowDropDown,
                        contentDescription = null
                    )
                },
                modifier = Modifier.menuAnchor()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                exerciseList.forEach { exercise ->
                    DropdownMenuItem(
                        text = { Text(text = exercise.exercise_name) },
                        onClick = {
                            selectedExerciseName = exercise.exercise_name
                            selectedExerciseID = exercise.id
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = repetitions,
            onValueChange = { repetitions = it },
            label = { Text("Repetitions") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = sets,
            onValueChange = { sets = it },
            label = { Text("Sets") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = weight,
            onValueChange = { newWeight -> weight = newWeight},
            label = { Text("Weight") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = {
                Log.d("ExerciseForm", "Repetitions value: $repetitions")
                Log.d("ExerciseForm", "Sets value: $sets")
                Log.d("ExerciseForm", "Weight value: $weight")

                val repetitionsValue = repetitions.takeIf { it.isNotBlank() }?.toInt() ?: 0
                val setsValue = sets.takeIf { it.isNotBlank() }?.toInt() ?: 0
                val weightValue = weight.takeIf { it.isNotBlank() }?.toFloat() ?: 0f



                    coroutineScope.launch {
                        withContext(Dispatchers.IO) {
                            exerciseOnTrainingDao.addExerciseOnTraining(
                                ExerciseOnTraining(
                                    training_id = currentTrainingId,
                                    exercise_id = selectedExerciseID,
                                    repetitions = repetitionsValue,
                                    sets = setsValue,
                                    weight = weightValue
                                )
                            )
                        }
                    }



                selectedExerciseName = ""
                repetitions = ""
                sets = ""
                weight = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Dodaj ćwiczenie")
        }
    }
}
