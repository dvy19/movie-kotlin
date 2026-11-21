package com.example.movie.auth


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MovieFilter
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.delay

// Professional Cinema Dark Palette
private val CinemaObsidian = Color(0xFF0B0D12)
private val CinemaSurface = Color(0xFF151922)
private val CinemaGold = Color(0xFFE5A93B)
private val CinemaGoldMuted = Color(0x33E5A93B)
private val TextPrimary = Color(0xFFF3F4F6)
private val TextSecondary = Color(0xFF9CA3AF)

@Composable
fun SplashScreen(
    rootNavController: NavHostController,
    splashRoute: String = "splash",
    mainRoute: String = "main",
    loginRoute: String = "login"
) {
    val logoScale = remember { Animatable(0.82f) }
    val contentAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        // Entrance animation
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
        )
    }

    val context= LocalContext.current
    val sessionManager=SessionManager(context)

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
        )

        // Minimum branding display window
        delay(1200L)




        val token = sessionManager.getAccessToken()
        val destination = if (!token.isNullOrBlank()) mainRoute else loginRoute

        rootNavController.navigate(destination) {
            popUpTo(splashRoute) {
                inclusive = true
            }
            launchSingleTop = true
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = CinemaObsidian
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF121620),
                            CinemaObsidian,
                            Color(0xFF07080B)
                        )
                    )
                )
        ) {
            // Center Branding Stack
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .scale(logoScale.value)
                    .alpha(contentAlpha.value),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(92.dp)
                        .background(color = CinemaSurface, shape = CircleShape)
                        .border(width = 1.5.dp, color = CinemaGoldMuted, shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Rounded.MovieFilter,
                        contentDescription = "App Logo",
                        tint = CinemaGold,
                        modifier = Modifier.size(46.dp)
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "CINEVERSE",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 3.sp
                    ),
                    color = TextPrimary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Discover • Discuss • Rate",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        letterSpacing = 1.sp
                    ),
                    color = TextSecondary
                )
            }

            // Bottom Subtle Loader
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
                    .alpha(contentAlpha.value),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(22.dp),
                    color = CinemaGold,
                    trackColor = CinemaSurface,
                    strokeWidth = 2.dp
                )
            }
        }
    }
}