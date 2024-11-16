package su.pank.sprintlens.ui.screen.select_dataset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.rounded.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import io.github.vinceglb.filekit.compose.rememberFilePickerLauncher
import io.github.vinceglb.filekit.core.PickerType
import io.github.vinceglb.filekit.core.PlatformFile
import org.jetbrains.compose.ui.tooling.preview.Preview
import su.pank.sprintlens.ui.components.Logo
import su.pank.sprintlens.ui.components.LogoPreview
import su.pank.sprintlens.ui.theme.AppTheme

data class Dataset(val name: String)

object SelectDatasetScreen : Screen {

    @Composable
    fun DatasetChooser(state: SelectDatasetState, onDataSetChoosed: (Dataset) -> Unit, loadNewDataset: () -> Unit) {
        val primary = MaterialTheme.colorScheme.primary
        val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
        Column(Modifier.widthIn(max = 274.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(buildAnnotatedString {
                append("Выбери ")

                withStyle(SpanStyle(color = primary)) {
                    append("датасет")
                }
            }, style = MaterialTheme.typography.headlineMedium)
            Spacer(Modifier.height(36.dp))
            LazyColumn(Modifier.heightIn(max = 300.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                when (state) {
                    is SelectDatasetState.Success -> {
                        val datasets = state.datasets
                        items(datasets) {
                            NavigationDrawerItem(label = {
                                Text("${it.name}")
                            }, onClick = {
                                onDataSetChoosed(it)
                            }, selected = true, icon = {
                                Icon(Icons.Default.Inbox, null)
                            })
                        }
                    }

                    SelectDatasetState.Loading -> {
                        items(3) {
                            NavigationDrawerItem(label = {
                                Box(
                                    Modifier.shimmer(shimmerInstance).size(400.dp, 20.dp)
                                        .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(12.dp))

                                )
                            }, onClick = {
                            }, selected = true, icon = {
                                Icon(Icons.Default.Inbox, null, modifier = Modifier.shimmer(shimmerInstance))
                            }, )
                        }
                    }

                    is SelectDatasetState.Error -> {
                        item {
                            Text("Произошла ошибка, пожалуйста проверьте подключение к интернету")
                        }
                    }
                }

            }
            Spacer(Modifier.height(10.dp))

            NavigationDrawerItem(
                label = {
                    Text("Загрузить")
                },
                onClick = {
                    loadNewDataset()
                },
                selected = true,
                icon = {
                    Icon(Icons.Default.Download, null)
                },
                modifier = Modifier.width(160.dp),
                colors = NavigationDrawerItemDefaults.colors(selectedContainerColor = MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun FlowRowScope.FileChooserButton(pickedFile: PlatformFile? = null, onFileChoosed: (PlatformFile) -> Unit) {
        val launcher = rememberFilePickerLauncher(type = PickerType.File(listOf("csv"))) {
            if (it != null)
                onFileChoosed(it)
        }


        Button(onClick = {
            launcher.launch()
        }) {
            Text("Выбрать")
        }
        if (pickedFile != null)
            Text("Выбран файл:${pickedFile.name}")



    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun FileChooserDialog(onCancellation: () -> Unit, onCompleteLoading: () -> Unit) {
        var isLoading by remember { mutableStateOf(false) }
        var sprintFile: PlatformFile? by remember {
            mutableStateOf(null)
        }
        var ticketFile: PlatformFile? by remember {
            mutableStateOf(null)
        }
        var historyTicketFile: PlatformFile? by remember {
            mutableStateOf(null)
        }
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.

            },
            icon = { Icon(Icons.Rounded.Download, contentDescription = null) },
            title = { Text(text = "Выберите файлы") },
            text = {
                // Спринты, тикеты и история тикетов
                Column {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Файл спринтов:")
                        FileChooserButton(sprintFile) {
                            sprintFile = it
                        }
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Файл тикетов:")
                        FileChooserButton(ticketFile) {
                            ticketFile = it
                        }
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Файл истории тикетов:")
                        FileChooserButton(historyTicketFile) {
                            historyTicketFile = it
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = onCompleteLoading) { Text("Загрузить") }
            },
            dismissButton = {
                TextButton(onClick = onCancellation) { Text("Отмена") }
            }
        )
    }


    @Composable
    override fun Content() {
        val screenModel: SelectDatasetScreenModel = koinScreenModel()
        val navigator = LocalNavigator.currentOrThrow
        val state by screenModel.state.collectAsState(SelectDatasetState.Loading)
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.widthIn(max = 442.dp).fillMaxWidth().align(Alignment.TopCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.weight(1f))
                Logo(modifier = Modifier.fillMaxWidth())
                Spacer(Modifier.weight(1f))
                DatasetChooser(state, {
                    screenModel.onChoosedDataset(it, navigator)
                }) {
                    screenModel.isChooseNewDataset = true
                }
                Spacer(Modifier.weight(2f))
            }
        }
        if (screenModel.isChooseNewDataset) {
            FileChooserDialog(onCancellation = { screenModel.isChooseNewDataset = false }) {}
        }
    }
}


@Preview
@Composable
fun SelectDatasetScreenPreview() {
    Navigator(SelectDatasetScreen)
}