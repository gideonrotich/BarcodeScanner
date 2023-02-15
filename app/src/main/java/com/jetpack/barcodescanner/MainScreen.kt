package com.jetpack.barcodescanner

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun MainScreen(
    navController: NavHostController,
) {
    Column() {
        Button(
            onClick = {
                navController.navigate(Screen.CameraScreen.route)
            },

            ) {
            Text(text = "Open Camera")
        }
    }
}