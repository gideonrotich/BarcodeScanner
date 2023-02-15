package com.jetpack.barcodescanner

import android.Manifest
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Home
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.common.util.concurrent.ListenableFuture
import com.jetpack.barcodescanner.camera.CameraPreview
import com.jetpack.barcodescanner.ui.theme.BarcodeScannerTheme
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BarcodeScannerTheme {
                Surface(color = MaterialTheme.colors.background) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(10.dp))

                        val navController = rememberNavController()
                        NavHost(
                            navController = navController,
                            startDestination = Screen.MainScreen.route
                        ) {
                            composable(
                                route = Screen.MainScreen.route
                            ) {
                                MultiplePermissions(navController)
                            }
                            composable(
                                route = Screen.CameraScreen.route
                            ) {
                                CameraPreview()
                            }
                            composable(
                                route = Screen.NextScreen.route
                            ) {
                                MainScreen(navController)
                            }
                        }

                    }
                }
            }
        }
    }
}


@ExperimentalPermissionsApi
@Composable
fun MultiplePermissions(navController: NavHostController) {
    val permissionStates = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(key1 = lifecycleOwner, effect = {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> {
                    permissionStates.launchMultiplePermissionRequest()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    })
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )
    {
        permissionStates.permissions.forEach { it ->
            when (it.permission) {
                Manifest.permission.CAMERA -> {
                    when {
                        it.hasPermission -> {
                            /* Permission has been granted by the user.
                               You can use this permission to now acquire the location of the device.
                               You can perform some other tasks here.
                            */
//                            Text(text = "Read Ext Storage permission has been granted")
                        }
                        it.shouldShowRationale -> {
                            /*Happens if a user denies the permission two times

                             */
                            Text(text = "Read Ext Storage permission is needed")
                        }
                        !it.hasPermission && !it.shouldShowRationale -> {
                            /* If the permission is denied and the should not show rationale
                                You can only allow the permission manually through app settings
                             */
                            Text(text = "Navigate to settings and enable the Storage permission")

                        }
                    }
                }
                Manifest.permission.ACCESS_FINE_LOCATION -> {
                    when {
                        it.hasPermission -> {
                            /* Permission has been granted by the user.
                               You can use this permission to now acquire the location of the device.
                               You can perform some other tasks here.
                            */
                            MainScreen(navController = navController)
                        }
                        it.shouldShowRationale -> {
                            /*Happens if a user denies the permission two times

                             */
                            Text(text = "Location permission is needed")

                        }
                        !it.hasPermission && !it.shouldShowRationale -> {
                            /* If the permission is denied and the should not show rationale
                                You can only allow the permission manually through app settings
                             */
                            Text(text = "Navigate to settings and enable the Location permission")

                        }
                    }
                }
            }
        }
    }
}




















