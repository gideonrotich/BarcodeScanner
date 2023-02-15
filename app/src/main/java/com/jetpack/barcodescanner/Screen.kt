package com.jetpack.barcodescanner


sealed class Screen(val route: String) {
    object MainScreen : Screen("main_screen")
    object CameraScreen : Screen("camera_screen")
    object NextScreen : Screen("next_screen")
}
