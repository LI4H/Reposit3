package com.example.project2

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.project2.screens.axxonOne.data.ServerDataModel
import com.example.project2.screens.axxonOne.data.ServerDataScreen
import com.example.project2.screens.camera.CameraView
import com.example.project2.structure.axxonOne.CameraWithBitmap
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val serverDataModel: ServerDataModel = viewModel()

            NavHost(navController = navController, startDestination = "server_version") {
                composable("server_version") {
                    ServerDataScreen { camera ->
                        Log.d(
                            "MainActivity",
                            "Navigating to camera_view with ID: ${camera.displayId}"
                        )
                        navController.navigate("camera_view/${camera.displayId}")
                    }
                }
                composable(
                    route = "camera_view/{cameraId}",
                    arguments = listOf(navArgument("cameraId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val cameraId = backStackEntry.arguments?.getString("cameraId")
                    Log.d("MainActivity", "camera_view screen opened for ID: $cameraId")
                    val cameraWithSnapshot = serverDataModel.serverDataState.value.cameras
                        .find { it.camera.displayId == cameraId }
                        ?: throw RuntimeException("MainActivity - Camera not found")

                    var cameraWithBitmap by remember { mutableStateOf<CameraWithBitmap?>(null) }
                    val scope = rememberCoroutineScope()

                    LaunchedEffect(cameraId) {
                        scope.launch {
                            cameraWithBitmap =
                                serverDataModel.loadCameraWithBitmap(cameraWithSnapshot)
                        }
                    }

                    cameraWithBitmap?.let {
                        CameraView(
                            camera = it.camera,
                            snapshot = it.snapshot
                        )
                    } ?: Text("Loading...")
                }


            }
        }
    }
}
