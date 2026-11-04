package com.example.simplecameraapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import com.example.simplecameraapp.ui.theme.SimpleCameraAppTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import android.graphics.Bitmap
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SimpleCameraAppTheme {
                CameraScreen()
            }
        }
    }
}

@Composable
fun CameraScreen() {
    var capturedImage by remember {
        mutableStateOf<Bitmap?>(null)
    }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) {
        bitmap ->
        capturedImage = bitmap
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Simple Camera",
            style = MaterialTheme.typography.headlineMedium
        )
        if (capturedImage != null) {
            Image(
                bitmap = capturedImage!!.asImageBitmap(),
                contentDescription = "Captured Photo",
                modifier = Modifier
                    .size(250.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(onClick = {
            cameraLauncher.launch(null)
        }) {
            Text("Capture Photo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { }) {
            Text("Share Photo")
        }
    }
}