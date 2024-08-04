package com.example.apollotracker.view.screen

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.times
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.apollotracker.view.components.ApolloTrackerLogoWithText
import com.example.apollotracker.viewmodel.SplashViewModel

@Composable
fun SplashScreen() {
    val viewModel: SplashViewModel = hiltViewModel()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFF1E1E1E), Color(0xFF121212))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ApolloTrackerLogoWithText()

                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Track Your Cryptocurrency Prices Instantly",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Light,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(32.dp))

                InfiniteProgressBar()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onAction(SplashViewModel.Action.StartTimer)
    }
}

@Composable
fun InfiniteProgressBar() {
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .clip(RoundedCornerShape(2.dp))
            .background(Color.Gray.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(fraction = 0.2f)
                .offset(x = progress * (LocalContext.current.resources.displayMetrics.widthPixels.toFloat() * 0.8f).dp)
                .clip(RoundedCornerShape(2.dp))
                .background(Color.White)
        )
    }
}
