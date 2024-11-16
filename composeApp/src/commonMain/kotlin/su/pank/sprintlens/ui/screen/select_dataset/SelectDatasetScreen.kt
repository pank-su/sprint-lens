package su.pank.sprintlens.ui.screen.select_dataset

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import org.jetbrains.compose.ui.tooling.preview.Preview
import su.pank.sprintlens.ui.components.Logo
import su.pank.sprintlens.ui.components.LogoPreview
import su.pank.sprintlens.ui.theme.AppTheme

data class Dataset(val name: String)

object SelectDatasetScreen : Screen {

    @Composable
    fun DatasetChooser(datasets: List<Dataset>, onDataSetChoosed: (Dataset) -> Unit) {
        val primary = MaterialTheme.colorScheme.primary
        Column(Modifier.widthIn(max = 274.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(buildAnnotatedString {
                append("Выбери ")

                withStyle(SpanStyle(color = primary)) {
                    append("датасет")
                }
            }, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(36.dp))
            LazyColumn(Modifier.heightIn(max = 300.dp)) {
                items(datasets) {
                    NavigationDrawerItem(label = {
                        Text("${it.name}")
                    }, onClick = {}, selected = true, icon = {
                        Icon(Icons.Default.Inbox, null)
                    })
                }
            }
            Spacer(Modifier.height(10.dp))

            NavigationDrawerItem(
                label = {
                    Text("Загрузить")
                },
                onClick = {},
                selected = true,
                icon = {
                    Icon(Icons.Default.Download, null)
                },
                modifier = Modifier.width(156.dp),
                colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }


    @Composable
    override fun Content() {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.widthIn(max = 442.dp).fillMaxWidth().align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.weight(1f))
                Logo(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.weight(1f))
                DatasetChooser(listOf(Dataset("Датасет 1"))) {}
                Spacer(Modifier.weight(2f))

            }
        }
    }
}

@Preview
@Composable
fun DatasetChooserPreview() {
    SelectDatasetScreen.DatasetChooser(listOf(Dataset("Датасет 1"))) {}
}


@Preview
@Composable
fun SelectDatasetScreenPreview() {
    Navigator(SelectDatasetScreen)
}