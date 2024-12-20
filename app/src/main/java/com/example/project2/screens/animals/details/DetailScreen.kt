package com.example.project2.screens.animals.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.project2.ui.theme.values.M
import com.example.project2.ui.theme.values.S
import com.example.project2.ui.theme.values.text_L
import com.example.project2.ui.theme.values.text_M


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimalDetailScreen(
    navController: NavController,
    animalId: Long
) {

    val detailViewModel: DetailViewModel = viewModel()
    val state by detailViewModel.state.collectAsState()

    DisposableEffect(animalId) {
        detailViewModel.init(animalId)
        onDispose {

        }
    }
    //
    /*val animal = animalId?.let { id ->
        state.animals.find { it.id == id }
    }
    */

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Animal Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(M),
            verticalArrangement = Arrangement.spacedBy(S)
        ) {
            if (state.animal != null) {
                Text(
                    text = "Animal Type: ${state.animal!!.type}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = text_L
                    )
                )
                Text(
                    text = "Name: ${state.animal!!.name}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        fontSize = text_M
                    )
                )
                Text(
                    text = "Color: ${state.animal!!.color}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontStyle = FontStyle.Italic,
                        fontSize = text_M
                    )
                )
                Text(
                    text = "Description: ${state.animal!!.describe()}",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = text_M
                    )
                )
            } else {
                Text(
                    text = "Animal not found.",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = text_L
                    )
                )
            }
        }
    }
}