package com.example.project2.screens.axxonOne.data


import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.project2.structure.axxonOne.Camera
import com.example.project2.structure.axxonOne.CameraWithBitmap
import com.example.project2.structure.axxonOne.CameraWithSnapshot
import com.example.project2.utils.ServerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

class ServerDataModel : ViewModel() {

    private val _serverDataState = MutableStateFlow(ServerDataState())
    val serverDataState: StateFlow<ServerDataState> get() = _serverDataState
    private val client = OkHttpClient()

    fun loadServerVersion() {
        viewModelScope.launch(Dispatchers.IO) {
            ServerRepository.fetchServerVersion().collect { version ->
                _serverDataState.value = _serverDataState.value.copy(version = version)
            }
        }
    }

    suspend fun loadSnapshot(camera: Camera): ByteArray? {
        return try {
            camera.videoStreams.firstOrNull()?.accessPoint?.let {
                ServerRepository.getSnapshot(it)
            }
        } catch (e: Exception) {
            Log.e("ServerDataModel", "Error loading snapshot for camera ${camera.displayId}: $e")
            null
        }
    }


    //todo
    fun loadCameras() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val cameras = ServerRepository.getCameras()
                _serverDataState.value = _serverDataState.value.copy(
                    cameras = cameras.map { CameraWithSnapshot(it, null) })
//                val camerasWithSnapshots = cameras.map { camera ->
//                    val snapshotUrl = camera.videoStreams.firstOrNull()?.accessPoint
//                    Log.d("Server Data Model", "VideoStreams URL before cleaning: $snapshotUrl")
//                   val cleanUrl = snapshotUrl?.replaceFirst("hosts/", "")
//                   Log.d("Server Data Model", "VideoStream URL after cleaning in : $cleanUrl")
//                    CameraWithSnapshot(camera, cleanUrl)//todo
//                }
//                _serverDataState.value = _serverDataState.value.copy(cameras = camerasWithSnapshots)
            } catch (e: Exception) {
                Log.e("ServerDataModel", "Error loading cameras: $e")
            }
        }
    }

    fun loadCameraWithBitmap(cameraWithSnapshot: CameraWithSnapshot): CameraWithBitmap {
        val bitmap = cameraWithSnapshot.snapshotUrl?.let { url ->
            try {
                val bytes = loadSnapshotFromUrl(url) // Реализуйте метод загрузки байтов из URL
                bytes?.let { BitmapFactory.decodeByteArray(bytes, 0, it.size) }
            } catch (e: Exception) {
                null // Обработка ошибок загрузки
            }
        }
        return CameraWithBitmap(
            camera = cameraWithSnapshot.camera,
            snapshot = bitmap
        )
    }

    private fun loadSnapshotFromUrl(url: String): ByteArray? {
        return try {
            val request = Request.Builder()
                .url(url)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                response.body?.bytes()
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

    }


}
    //todo сделать экран со списком камер
    // каждая камера самостоятельна и грузит свою картинку своей моделью


//    fun loadCamerasOld() {
//        viewModelScope.launch {
//            try {
//                val camerasWithSnapshots = ServerRepository.fetchCamerasWithSnapshots()
//                _serverDataState.value = _serverDataState.value.copy(
//                    cameras = camerasWithSnapshots,
//                    cameraCount = camerasWithSnapshots.size
//                )
//            } catch (e: Exception) {
//                _serverDataState.value = _serverDataState.value.copy(
//                    cameras = emptyList(),
//                    cameraCount = 0
//                )
//                Log.e("LoadCamerasError", "Error loading cameras: $e")
//            }
//        }
//    }
//
