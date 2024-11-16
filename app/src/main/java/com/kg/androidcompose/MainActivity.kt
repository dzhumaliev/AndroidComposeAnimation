package com.kg.androidcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Animatable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kg.androidcompose.ui.theme.AndroidComposeTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

//            MyApp()
//            AnimatedButton()
//            AnimatedBox() loading data
//            PulsatingCircle()
//            VisibilityAnimation()
//            ColorAndSizeAnimation()
//            ColorAnimationBox()
            MoveAnimationBox()
        }
    }
}

@Composable
fun AnimatedButton() {
    var isExpanded by remember { mutableStateOf(false) }
    val size by animateDpAsState(targetValue = if (isExpanded) 200.dp else 100.dp)

    Button(
        onClick = { isExpanded = !isExpanded },
        modifier = Modifier.size(size)
    ) {
        Text(text = "Нажми меня")
    }
}

@Composable
fun AnimatedBox() {
    val offset = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        offset.animateTo(
            targetValue = 300f,
            animationSpec = tween(durationMillis = 90000, easing = LinearOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .size(100.dp)
            .offset(x = offset.value.dp)
            .background(Color.Blue)
    )
}

@Composable
fun PulsatingCircle() {
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )


    Box(
        modifier = Modifier
            .size(400.dp)
            .scale(scale)
            .background(Color.Red, shape = CircleShape)
    )
}

@Composable
fun VisibilityAnimation() {
    var isVisible by remember { mutableStateOf(true) }

    Column {
        Button(onClick = { isVisible = !isVisible }) {
            Text("Переключить видимость")
        }

        AnimatedVisibility(visible = isVisible) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Green)
            )
        }
    }
}


@Composable
fun ColorAndSizeAnimation() {
    var isExpanded by remember { mutableStateOf(false) }
    val transition = updateTransition(targetState = isExpanded, label = "transition")

    val color by transition.animateColor(label = "color") {
        if (it) Color.Red else Color.Blue
    }

    val size by transition.animateDp(label = "size") {
        if (it) 200.dp else 100.dp
    }

    Box(
        modifier = Modifier
            .size(size)
            .background(color)
            .clickable { isExpanded = !isExpanded }
    )
}

// Advanced animation

@Composable
fun ColorAnimationBox() {
    val color = remember { Animatable(Color.Red) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        color.animateTo(
            targetValue = Color.Blue,
            animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
        )
    }

    Box(
        modifier = Modifier
            .size(200.dp)
            .background(color.value)
            .clickable {
//                 Перезапуск анимации при клике
                scope.launch {
                    color.animateTo(
                        targetValue = if (color.value == Color.Red) Color.Blue else Color.Red,
                        animationSpec = tween(durationMillis = 2000)
                    )
                }
            }
    )
}


@Composable
fun MoveAnimationBox() {
    val offsetX = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    // Начальная анимация при старте Composable
    LaunchedEffect(Unit) {
        offsetX.animateTo(
            targetValue = 100f,
            animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy,
                stiffness = Spring.StiffnessLow )
        )
    }

    Box(
        modifier = Modifier
            .offset(x = offsetX.value.dp)
            .size(200.dp)
            .background(Color.Green)
            .clickable {
                // Запускаем анимацию при клике
                scope.launch {
                    val newValue = if (offsetX.value > 0f) 0f else 100f
                    offsetX.animateTo(
                        targetValue = newValue,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy)
                    )
                }
            }
    )
}


@Composable
fun MyApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("details") {
            DetailsScreen(navController)
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .background(Color.Red)
            .fillMaxSize()
    ) {
        Text("Home Screen")
        Button(onClick = {
            navController.navigate("details")
        }) {
            Text("Перейти в детальную страницу")
        }
    }
}


@Composable
fun DetailsScreen(navController: NavHostController) {
    Column(modifier = Modifier.background(Color.Blue)) {
        Text("DetailScreen")
        Button(onClick = {
            navController.popBackStack()
        }) {
            Text("Назад в Главную страницу")
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidComposeTheme {
//        MyApp()
//        AnimatedButton()
//        AnimatedBox()
//        PulsatingCircle()
//        VisibilityAnimation()
//        ColorAndSizeAnimation()
//        ColorAnimationBox()
        MoveAnimationBox()
    }
}