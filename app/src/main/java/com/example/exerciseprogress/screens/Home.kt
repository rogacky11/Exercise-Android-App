package com.example.exerciseprogress.screens

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.example.exerciseprogress.data.ExerciseDatabase
import com.example.exerciseprogress.data.Training
import com.example.exerciseprogress.ui.theme.Screens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(navigationController: NavController, context: Context) {
    var dateState = rememberDatePickerState()


    val db = remember { ExerciseDatabase.getDatabase(context) }
    val trainingDao = remember { db.trainingDao() }
    val trainings: LiveData<List<Training>> = trainingDao.getAllTrainings()
    val trainingList by trainings.observeAsState(emptyList())

    var trainingDate by rememberSaveable { mutableStateOf("") }
    var showDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 0.dp, 20.dp, 25.dp),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.End
    ) {
        FloatingActionButton(
            onClick = { showDialog = true },
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Floating action button.")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        trainingDate = dateState.selectedDateMillis?.let { millis ->
                            // Format the date as needed, for example to a string
                            val date = java.text.SimpleDateFormat(
                                "yyyy-MM-dd",
                                java.util.Locale.getDefault()
                            )
                            date.format(java.util.Date(millis))
                        } ?: ""

                        showDialog = false
                        coroutineScope.launch {
                            withContext(Dispatchers.IO) {
                                trainingDao.addTraining(
                                    Training(
                                        id = 0,
                                        training_date = trainingDate
                                    )
                                )
                            }
                            showDialog = false
                        }
                        navigationController.navigate(Screens.Home.CreateTraining.screen)
                    }
                ) {
                    Text("Dodaj")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showDialog = false
                    }
                ) {
                    Text("Anuluj")
                }
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Trening nr.",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    DatePicker(
                        headline = { Text(text = "Wybierz datę") },
                        state = dateState,
                        showModeToggle = true
                    )
                }
            }
        )
    }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "Lista treningów:",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn {
            items(trainingList) { training ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .clickable {
                            navigationController.navigate("${Screens.TrainingDetail.screen}/${training.id}")
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = training.training_date, fontSize = 20.sp)
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                withContext(Dispatchers.IO) {
                                    trainingDao.deleteTraining(training)
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Filled.Delete, contentDescription = "Delete training")
                    }
                }
            }
        }
    }
}







