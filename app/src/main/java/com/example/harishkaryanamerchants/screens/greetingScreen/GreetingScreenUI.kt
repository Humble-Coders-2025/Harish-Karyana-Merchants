package com.example.harishkaryanamerchants.screens.greetingScreen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.harishkaryanamerchants.R
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.dp

@Composable
fun GreetingScreen(onGetStartedClick: () -> Unit = {}) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    var startAnimation by remember { mutableStateOf(false) }

    val girlOffset by animateDpAsState(
        targetValue = if (startAnimation) (-30).dp else -screenWidth, // Remove .dp from -screenWidth
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "Girl Animation"
    )


    val boyOffset by animateDpAsState(
        targetValue = if (startAnimation) 30.dp else screenWidth,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing), label = "Boy Animation"
    )

    LaunchedEffect(Unit) {
        delay(300)
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xCC580768))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .zIndex(1f)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ellipse_greeting),
                contentDescription = "Background Overlay",
                contentScale = ContentScale.FillWidth,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .zIndex(2f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                listOf("Harish", "KARYANA", "MERCHANTS").forEach {
                    Text(
                        text = it,
                        color = Color.White,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        letterSpacing = 1.sp,
                        style = TextStyle(
                            shadow = Shadow(
                                color = Color.Black.copy(alpha = 0.8f),
                                offset = Offset(4f, 4f),
                                blurRadius = 10f
                            )
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(-100.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.offset(x = girlOffset).zIndex(2f)) {
                        Image(
                            painter = painterResource(id = R.drawable.greeting_girl),
                            contentDescription = "Girl with groceries",
                            modifier = Modifier.size(screenWidth * 0.6f)
                        )
                    }
                    Box(modifier = Modifier.offset(x = boyOffset).zIndex(1f)) {
                        Image(
                            painter = painterResource(id = R.drawable.greeting_boy),
                            contentDescription = "Boy with groceries",
                            modifier = Modifier.size(screenWidth * 0.6f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 100.dp)
            ) {
                Text(
                    text = "Grocery Shopping Made Easy",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 20.dp),
                )

                Button(
                    onClick = onGetStartedClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White
                    )
                ) {
                    Text(
                        text = "Get Started",
                        color = Color(0xFF673AB7),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}