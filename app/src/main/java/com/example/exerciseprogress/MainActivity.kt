package com.example.exerciseprogress

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.exerciseprogress.screens.AddExerciseScreen
import com.example.exerciseprogress.screens.ExerciseList
import com.example.exerciseprogress.ui.theme.GreenJC
import com.example.exerciseprogress.screens.Home
import com.example.exerciseprogress.screens.Progress
import com.example.exerciseprogress.ui.theme.Screens
import com.example.exerciseprogress.screens.TrainingDetailScreen
import com.example.exerciseprogress.data.ExerciseDatabase
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            NavDrawer(context)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun NavDrawer(context: Context) {

        val db = ExerciseDatabase.getDatabase(context)
        val exerciseDao = db.exerciseDao()
        val exerciseOnTrainingDao = db.exerciseOnTrainingDao()

        val navigationController = rememberNavController()
        val coroutineScope = rememberCoroutineScope()
        val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
        val context = LocalContext.current

        val selected = remember { mutableStateOf(Icons.Default.Home) }

        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = true,
            drawerContent = {
                ModalDrawerSheet {
                    Box(
                        modifier = Modifier
                            .background(GreenJC)
                            .fillMaxWidth()
                            .height(65.dp)
                    ) {
                        Text(
                            text = "Menu",
                            fontSize = 25.sp,
                            color = Color.White,
                            modifier = Modifier.padding(15.dp)
                        )
                    }
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = "Treningi") },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "home",
                                tint = GreenJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.Home.screen) {
                                popUpTo(0)
                            }
                        })
                    NavigationDrawerItem(
                        label = { Text(text = "Lista ćwiczeń") },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "exercise list",
                                tint = GreenJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.ExerciseList.screen) {
                                popUpTo(0)
                            }
                        })
                    NavigationDrawerItem(
                        label = { Text(text = "Postępy") },
                        selected = false,
                        icon = {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "progress",
                                tint = GreenJC
                            )
                        },
                        onClick = {
                            coroutineScope.launch {
                                drawerState.close()
                            }
                            navigationController.navigate(Screens.Progress.screen) {
                                popUpTo(0)
                            }
                        })
                }
            },
        ) {
            Scaffold(
                topBar = {
                    val coroutineScope = rememberCoroutineScope()
                    TopAppBar(
                        title = { Text(text = "Obczaj formę byku") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = GreenJC,
                            titleContentColor = Color.White,
                            navigationIconContentColor = Color.White
                        ),
                        navigationIcon = {
                            IconButton(onClick = {
                                coroutineScope.launch {
                                    drawerState.open()
                                }
                            }) {
                                Icon(Icons.Rounded.Menu, contentDescription = "MenuButton")
                            }
                        },
                    )
                }
            ) {
                NavHost(
                    navController = navigationController,
                    startDestination = Screens.Home.screen
                ) {
                    composable(Screens.Home.screen) {
                        Home(
                            navigationController = navigationController,
                            context = context
                        )
                    }
                    composable(Screens.ExerciseList.screen) { ExerciseList(context = context) }
                    composable(Screens.Progress.screen) { Progress() }
                    composable(Screens.Home.CreateTraining.screen) {
                        AddExerciseScreen(
                            navigationController = navigationController,
                            context = context,
                            )
                    }
                    composable("${Screens.TrainingDetail.screen}/{trainingId}") { backStackEntry ->
                        val trainingId =
                            backStackEntry.arguments?.getString("trainingId")?.toIntOrNull() ?: 0
                        TrainingDetailScreen(trainingId = trainingId, context = context)

                    }
                }
            }
        }
    }
}
