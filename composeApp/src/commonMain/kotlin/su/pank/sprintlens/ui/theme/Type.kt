package su.pank.sprintlens.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import sprintlens.composeapp.generated.resources.Res
import sprintlens.composeapp.generated.resources.space

val spaceGrotesk
    @Composable
    get() = FontFamily(Font(Res.font.space))

val AppTypography = Typography()

val Typography.logoTitle
    @Composable
    get() = TextStyle(fontFamily = spaceGrotesk, fontWeight = FontWeight.Bold, fontSize = 57.sp, lineHeight = 64.sp)