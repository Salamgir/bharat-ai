package com.example.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.R
import com.example.data.ToolAvailability
import com.example.ui.components.ToolAvailabilityBadge
import com.example.ui.theme.NavyDeep
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SaffronPrimary

private enum class BharatPhotoPreset(
    val label: String,
    val brightnessBoost: Float,
    val contrastBoost: Float,
    val saturationBoost: Float,
    val warmthTint: Float
) {
    ORIGINAL("Original", 0f, 1f, 1f, 0f),
    SAFFRON_GLOW("Saffron Glow", 10f, 1.12f, 1.25f, 22f),
    ROYAL_NAVY("Royal Navy", -6f, 1.18f, 0.88f, -18f),
    VIVID_BHARAT("Vivid Bharat", 8f, 1.15f, 1.45f, 8f),
    GOLDEN_HOUR("Golden Hour", 12f, 1.05f, 1.2f, 28f),
    CLASSIC_BW("Classic B&W", 4f, 1.22f, 0f, 0f)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhotoEditorScreen(
    onSavePreview: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var activePreset by rememberSaveable { mutableStateOf(BharatPhotoPreset.ORIGINAL) }
    var brightness by rememberSaveable { mutableFloatStateOf(0f) }
    var contrast by rememberSaveable { mutableFloatStateOf(1f) }
    var saturation by rememberSaveable { mutableFloatStateOf(1f) }
    var rotationDegrees by rememberSaveable { mutableIntStateOf(0) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
        }
    }

    val combinedMatrix = remember(activePreset, brightness, contrast, saturation) {
        buildColorMatrix(
            brightness = brightness + activePreset.brightnessBoost,
            contrast = contrast * activePreset.contrastBoost,
            saturation = if (activePreset == BharatPhotoPreset.CLASSIC_BW) 0f else saturation * activePreset.saturationBoost,
            warmth = activePreset.warmthTint
        )
    }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Photo Editor", style = MaterialTheme.typography.titleLarge)
                        Spacer(modifier = Modifier.width(8.dp))
                        ToolAvailabilityBadge(
                            availability = ToolAvailability.ON_DEVICE,
                            label = "On-Device"
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("photo_editor_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            activePreset = BharatPhotoPreset.ORIGINAL
                            brightness = 0f
                            contrast = 1f
                            saturation = 1f
                            rotationDegrees = 0
                        },
                        modifier = Modifier.testTag("photo_editor_reset_button")
                    ) {
                        Icon(Icons.Filled.Refresh, contentDescription = "Reset adjustments")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .navigationBarsPadding(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 760.dp)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Canvas Preview Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyDeep),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(270.dp)
                            .padding(10.dp)
                            .clip(RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        val painter = if (selectedImageUri != null) {
                            rememberAsyncImagePainter(model = selectedImageUri)
                        } else {
                            painterResource(id = R.drawable.img_sample_photo)
                        }
                        Image(
                            painter = painter,
                            contentDescription = "Photo editor preview canvas",
                            contentScale = ContentScale.Crop,
                            colorFilter = ColorFilter.colorMatrix(combinedMatrix),
                            modifier = Modifier
                                .fillMaxSize()
                                .graphicsLayer {
                                    rotationZ = rotationDegrees.toFloat()
                                }
                        )
                    }
                }

                // Pick Photo & Rotate Controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .testTag("photo_pick_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                    ) {
                        Icon(
                            imageVector = Icons.Filled.AddPhotoAlternate,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Pick Photo")
                    }

                    OutlinedButton(
                        onClick = { rotationDegrees = (rotationDegrees + 90) % 360 },
                        modifier = Modifier
                            .weight(1f)
                            .heightIn(min = 48.dp)
                            .testTag("photo_rotate_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.4f))
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.RotateRight,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rotate (${rotationDegrees}°)")
                    }
                }

                // Filter Presets
                Column {
                    Text(
                        text = "Bharat Studio Presets",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        BharatPhotoPreset.entries.forEach { preset ->
                            FilterChip(
                                selected = activePreset == preset,
                                onClick = { activePreset = preset },
                                label = { Text(preset.label) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SaffronPrimary,
                                    selectedLabelColor = PureWhite
                                ),
                                modifier = Modifier
                                    .heightIn(min = 48.dp)
                                    .testTag("photo_preset_${preset.name.lowercase()}")
                            )
                        }
                    }
                }

                // Manual Sliders
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        EditorSlider(
                            label = "Brightness",
                            value = brightness,
                            valueRange = -60f..60f,
                            displayValue = "${brightness.toInt()}",
                            onValueChange = { brightness = it }
                        )
                        EditorSlider(
                            label = "Contrast",
                            value = contrast,
                            valueRange = 0.5f..1.8f,
                            displayValue = "${(contrast * 100).toInt()}%",
                            onValueChange = { contrast = it }
                        )
                        EditorSlider(
                            label = "Saturation",
                            value = saturation,
                            valueRange = 0f..2f,
                            displayValue = "${(saturation * 100).toInt()}%",
                            onValueChange = { saturation = it }
                        )
                    }
                }

                Button(
                    onClick = {
                        val savedLocation = saveEditedPhotoLocally(
                            context = context,
                            imageUri = selectedImageUri,
                            colorMatrixValues = combinedMatrix.values,
                            rotationDegrees = rotationDegrees
                        )
                        onSavePreview()
                        val msg = if (savedLocation != null) {
                            "Saved edited photo ($savedLocation)!"
                        } else {
                            "Edited preview saved to local studio!"
                        }
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 52.dp)
                        .testTag("photo_save_preview_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SaffronPrimary,
                        contentColor = PureWhite
                    )
                ) {
                    Icon(Icons.Filled.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Apply & Save Preview (On-Device)")
                }
            }
        }
    }
}

private fun saveEditedPhotoLocally(
    context: android.content.Context,
    imageUri: Uri?,
    colorMatrixValues: FloatArray,
    rotationDegrees: Int
): String? {
    return runCatching {
        val sourceBitmap = if (imageUri != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                val source = android.graphics.ImageDecoder.createSource(context.contentResolver, imageUri)
                android.graphics.ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.isMutableRequired = true
                }
            } else {
                @Suppress("DEPRECATION")
                android.provider.MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }
        } else {
            android.graphics.BitmapFactory.decodeResource(context.resources, R.drawable.img_sample_photo)
        } ?: return null

        val working = sourceBitmap.copy(android.graphics.Bitmap.Config.ARGB_8888, true)
        val filtered = android.graphics.Bitmap.createBitmap(
            working.width,
            working.height,
            android.graphics.Bitmap.Config.ARGB_8888
        )
        val canvas = android.graphics.Canvas(filtered)
        val paint = android.graphics.Paint().apply {
            colorFilter = android.graphics.ColorMatrixColorFilter(
                android.graphics.ColorMatrix(colorMatrixValues)
            )
        }
        canvas.drawBitmap(working, 0f, 0f, paint)

        val finalBitmap = if (rotationDegrees % 360 != 0) {
            val matrix = android.graphics.Matrix().apply { postRotate(rotationDegrees.toFloat()) }
            android.graphics.Bitmap.createBitmap(filtered, 0, 0, filtered.width, filtered.height, matrix, true)
        } else {
            filtered
        }

        val fileName = "bharat_ai_edit_${System.currentTimeMillis()}.jpg"
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
            val values = android.content.ContentValues().apply {
                put(android.provider.MediaStore.Images.Media.DISPLAY_NAME, fileName)
                put(android.provider.MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(android.provider.MediaStore.Images.Media.RELATIVE_PATH, "Pictures/BHARAT AI")
            }
            val outUri = context.contentResolver.insert(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                values
            )
            if (outUri != null) {
                context.contentResolver.openOutputStream(outUri)?.use { stream ->
                    finalBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 92, stream)
                }
                return "Pictures/BHARAT AI"
            }
        }

        val outFile = java.io.File(context.filesDir, fileName)
        java.io.FileOutputStream(outFile).use { stream ->
            finalBitmap.compress(android.graphics.Bitmap.CompressFormat.JPEG, 92, stream)
        }
        "App Studio Folder"
    }.getOrNull()
}

@Composable
private fun EditorSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    displayValue: String,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, style = MaterialTheme.typography.labelLarge)
            Text(
                text = displayValue,
                style = MaterialTheme.typography.labelMedium,
                color = SaffronPrimary
            )
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = valueRange,
            colors = SliderDefaults.colors(
                thumbColor = SaffronPrimary,
                activeTrackColor = SaffronPrimary
            )
        )
    }
}

private fun buildColorMatrix(
    brightness: Float,
    contrast: Float,
    saturation: Float,
    warmth: Float
): ColorMatrix {
    val satMatrix = ColorMatrix().apply { setToSaturation(saturation) }
    val scale = contrast
    val translate = (-0.5f * scale + 0.5f) * 255f + brightness
    val contrastWarmthMatrix = ColorMatrix(
        floatArrayOf(
            scale, 0f, 0f, 0f, translate + warmth,
            0f, scale, 0f, 0f, translate + warmth * 0.25f,
            0f, 0f, scale, 0f, translate - warmth * 0.6f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    satMatrix.timesAssign(contrastWarmthMatrix)
    return satMatrix
}
