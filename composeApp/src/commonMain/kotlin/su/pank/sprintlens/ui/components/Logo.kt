package su.pank.sprintlens.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.ui.tooling.preview.Preview
import su.pank.sprintlens.ui.theme.AppTheme

import org.jetbrains.compose.resources.painterResource
import sprintlens.composeapp.generated.resources.Res
import sprintlens.composeapp.generated.resources.logo
import su.pank.sprintlens.ui.theme.logoTitle


@Composable
fun Logo(modifier: Modifier = Modifier, fontSize: TextUnit = 57.sp) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
        Row(modifier, verticalAlignment = Alignment.CenterVertically) {
            Icon(painterResource(Res.drawable.logo), "ecg heart")
            Spacer(Modifier.width(10.dp))
            Text("Sprint Lens", style = MaterialTheme.typography.logoTitle, fontSize = fontSize)
        }
    }
}

@Preview
@Composable
fun LogoPreview() {
    AppTheme {
        Logo(Modifier.size(200.dp))
    }
}