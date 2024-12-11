package com.example.project2.screens.camera

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.example.project2.structure.axxonOne.Camera
import com.example.project2.utils.Sizes.size_m


@Composable
fun CameraView(camera: Camera, snapshot: Bitmap?) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(size_m),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Camera: ${camera.displayName}")

        snapshot?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "Camera Snapshot",
                modifier = Modifier.size(200.dp)
            )
        } ?: Text(text = "No snapshot available")
    }
}


//@Composable
//fun CameraView(camera: Camera, viewModel: CameraModel = viewModel()) {
//    val state by viewModel.state.collectAsState()
//
//    LaunchedEffect(camera) {
//        viewModel.init(camera)
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(size_m),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Camera: ${state.name}")
//
//        state.bitmap?.let {
//            Image(
//                bitmap = it.asImageBitmap(),
//                contentDescription = "Camera Snapshot",
//                modifier = Modifier.size(200.dp)
//            )
//        } ?: Text(text = "Loading snapshot...")
//    }
//}
