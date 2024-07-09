package com.example.exerciseprogress.ui.theme

sealed class Screens(val screen: String) {
    data object Home: Screens("home"){
        data object CreateTraining: Screens("CreateTraining")
    }
    data object ExerciseList: Screens("ExerciseList")
    data object Progress: Screens("Progress")
    data object TrainingDetail: Screens("TrainingDetail")

}