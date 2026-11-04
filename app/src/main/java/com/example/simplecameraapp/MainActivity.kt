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
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import android.content.Context
import androidx.compose.ui.platform.LocalContext
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

        val context = LocalContext.current

        Button(
            onClick = {
                capturedImage?.let {
                    sharePhoto(context, it)
                }
            },
            enabled = capturedImage != null
        ) {
            Text("Share Photo")
        }
    }
}
fun sharePhoto(context: Context, bitmap: Bitmap) {

    val file = File(context.cacheDir, "captured_photo.jpg")

    val outputStream = FileOutputStream(file)

    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

    outputStream.close()

    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/jpeg"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }

    context.startActivity(
        Intent.createChooser(shareIntent, "Share Photo")
    )
}