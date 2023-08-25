package ru.neuron.sportapp.ui

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val crane_caption = Color.DarkGray
val crane_divider_color = Color.LightGray
private val craneRed = Color(0xFFE30425)
private val craneWhite = Color.White
private val cranePurple700 = Color(0xFF720D5D)
private val cranePurple800 = Color(0xFF5D1049)
private val cranePurple900 = Color(0xFF4E0D3A)

val craneColors = lightColorScheme(
    primary = cranePurple800,
    secondary = craneRed,
    surface = cranePurple900,
    onSurface = craneWhite,
    onPrimary = cranePurple700
)

val BottomSheetShape = RoundedCornerShape(
    topStart = 20.dp,
    topEnd = 20.dp,
    bottomStart = 0.dp,
    bottomEnd = 0.dp
)

@Composable
fun SportTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = craneColors,
        typography = craneTypography) {
        content()
    }
}
