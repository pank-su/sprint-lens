package su.pank.sprintlens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.Navigator
import org.koin.compose.KoinApplication
import su.pank.sprintlens.ui.di.uiModule
import su.pank.sprintlens.ui.screen.select_dataset.SelectDatasetScreen
import su.pank.sprintlens.ui.screen.select_dataset.SelectDatasetScreenPreview
import su.pank.sprintlens.ui.theme.AppTheme

@Composable
fun App() {
    KoinApplication({
        modules(uiModule)

    }) {
        AppTheme {
            Surface(color = MaterialTheme.colorScheme.surfaceContainer, modifier = Modifier.fillMaxSize()) {
                Navigator(SelectDatasetScreen)
            }
        }
    }
}