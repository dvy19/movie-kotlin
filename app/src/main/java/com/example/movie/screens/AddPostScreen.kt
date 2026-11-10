package com.example.movie.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

// --- Cinematic Dark Theme Color Palette ---
val MovieDarkBackground = Color(0xFF0D0E12)
val MovieDarkSurface = Color(0xFF16181F)
val MovieDarkSurfaceVariant = Color(0xFF222530)
val MoviePrimaryAccent = Color(0xFFFFB703) // Cinematic Gold
val MovieOnPrimary = Color(0xFF111111)
val MovieTextPrimary = Color(0xFFF1F2F6)
val MovieTextSecondary = Color(0xFF9EA2B2)
val MovieBorderColor = Color(0xFF2E3242)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    onNavigateBack: () -> Unit,
    mainNavController: NavController,
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var isSubmitting by remember { mutableStateOf(false) }

    // Image Picker Launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    val isFormValid = title.isNotBlank() && content.isNotBlank()

    Scaffold(
        containerColor = MovieDarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Discussion",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MovieTextPrimary
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MovieTextPrimary
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            if (isFormValid && !isSubmitting) {
                                isSubmitting = true
                                onSubmitPost(title.trim(), content.trim(), selectedImageUri)
                            }
                        },
                        enabled = isFormValid && !isSubmitting,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MoviePrimaryAccent,
                            disabledContentColor = MovieTextSecondary.copy(alpha = 0.4f)
                        )
                    ) {
                        if (isSubmitting) {
                            CircularIndicatorSmall()
                        } else {
                            Text(
                                text = "Post",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MovieDarkBackground
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Community hint banner
            Card(
                colors = CardDefaults.cardColors(containerColor = MovieDarkSurface),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Movie,
                        contentDescription = null,
                        tint = MoviePrimaryAccent,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Share your reviews, theories, or film recommendations with the community.",
                        style = MaterialTheme.typography.bodySmall.copy(color = MovieTextSecondary)
                    )
                }
            }

            // Title Input Field
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "TITLE",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MovieTextSecondary,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    placeholder = {
                        Text(
                            text = "e.g., Why the cinematography in Dune: Part Two is generational...",
                            color = MovieTextSecondary.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MovieDarkSurface,
                        unfocusedContainerColor = MovieDarkSurface,
                        disabledContainerColor = MovieDarkSurface,
                        focusedBorderColor = MoviePrimaryAccent,
                        unfocusedBorderColor = MovieBorderColor,
                        focusedTextColor = MovieTextPrimary,
                        unfocusedTextColor = MovieTextPrimary,
                        cursorColor = MoviePrimaryAccent
                    ),
                    singleLine = true
                )
            }

            // Content Input Field
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "YOUR THOUGHTS",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MovieTextSecondary,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    placeholder = {
                        Text(
                            text = "Write your review, breakdown, or discussion points here...",
                            color = MovieTextSecondary.copy(alpha = 0.5f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MovieDarkSurface,
                        unfocusedContainerColor = MovieDarkSurface,
                        disabledContainerColor = MovieDarkSurface,
                        focusedBorderColor = MoviePrimaryAccent,
                        unfocusedBorderColor = MovieBorderColor,
                        focusedTextColor = MovieTextPrimary,
                        unfocusedTextColor = MovieTextPrimary,
                        cursorColor = MoviePrimaryAccent
                    )
                )
            }

            // Media / Image Attachment Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "ATTACH POSTER OR SCENE (OPTIONAL)",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MovieTextSecondary,
                        letterSpacing = 1.2.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )

                if (selectedImageUri == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MovieDarkSurface)
                            .border(
                                width = 1.dp,
                                color = MovieBorderColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = "Upload Image",
                                tint = MoviePrimaryAccent,
                                modifier = Modifier.size(32.dp)
                            )
                            Text(
                                text = "Tap to upload image",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = MovieTextSecondary
                                )
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MovieDarkSurface)
                    ) {
                        AsyncImage(
                            model = selectedImageUri,
                            contentDescription = "Selected Post Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        // Remove Image Button
                        IconButton(
                            onClick = { selectedImageUri = null },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(32.dp)
                                .background(Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(16.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove image",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun CircularIndicatorSmall() {
    Box(
        modifier = Modifier.size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            color = MoviePrimaryAccent,
            strokeWidth = 2.dp
        )
    }
}