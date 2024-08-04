package com.example.apollotracker.view.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Path
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment

@Composable
fun ApolloTrackerLogoWithText() {
    Column(
        modifier = Modifier.wrapContentSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ApolloTrackerLogo(modifier = Modifier.size(200.dp))
        Spacer(modifier = Modifier.height(16.dp))
        ApolloTrackerText()
    }
}

@Composable
private fun ApolloTrackerLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val pinPath = Path().apply {
            moveTo(size.width / 2, size.height / 4)
            lineTo(size.width / 2 + 100, size.height / 2)
            lineTo(size.width / 2 - 100, size.height / 2)
            close()
        }
        drawPath(path = pinPath, color = Color(0xFF0D47A1), style = Stroke(width = 20f))

        drawCircle(
            color = Color(0xFF1976D2),
            radius = 60f,
            center = this.center
        )

        drawCircle(
            color = Color.White,
            radius = 30f,
            center = this.center
        )
    }
}

@Composable
private fun ApolloTrackerText(modifier: Modifier = Modifier) {
    Text(
        text = "APOLLO TRACKER",
        fontSize = 40.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF0D47A1),
        modifier = modifier
    )
}
