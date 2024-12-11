package com.example.project2.screens.axxonOne.data

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.project2.structure.axxonOne.Camera
import com.example.project2.utils.Sizes.size_m
import com.example.project2.utils.Sizes.size_s
import com.example.project2.utils.Sizes.text_size_l
import kotlinx.coroutines.launch


@Composable
fun ServerDataScreen(viewModel: ServerDataModel = viewModel(), onCameraClick: (Camera) -> Unit) {
    val serverDataState by viewModel.serverDataState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadServerVersion()
        viewModel.loadCameras()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Server Version: ${serverDataState.version}",
            modifier = Modifier.padding(size_m),
            fontSize = text_size_l
        )

        LazyColumn(modifier = Modifier.padding(size_m)) {
            itemsIndexed(serverDataState.cameras) { index, cameraWithSnapshot ->
                val camera = cameraWithSnapshot.camera

                var snapshot by remember { mutableStateOf<Bitmap?>(null) }
                val scope = rememberCoroutineScope()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCameraClick(camera) }
                        .padding(size_s),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}. ${camera.displayName}",
                        modifier = Modifier.weight(1f)
                    )
                    snapshot?.let { bitmap ->
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                    } ?: Text(
                        text = "Loading...",
                        modifier = Modifier.clickable {
                            scope.launch {
                                try {
                                    val bytes = viewModel.loadSnapshot(camera)
                                    snapshot =
                                        bytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                                } catch (e: Exception) {
                                    Log.e("ServerDataScreen", "Error loading snapshot: $e")
                                }
                            }
                        }
                    )
                }
            }
        }
    }
}

//                Text(
//                    text = "${index + 1}. ${camera.displayName}",
//                    modifier = Modifier
//                        .padding(size_s)
//                        .clickable {
//                            Log.d("ServerDataScreen",
//                                "Camera clicked: ${camera.displayName}," +
//                                    "ID: ${camera.displayId}")//todo
//
//                            onCameraClick(camera) }
//                )
//            }
//        }
//    }
//}
