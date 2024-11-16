package su.pank.sprintlens.ui.screen.select_dataset

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import su.pank.sprintlens.data.DatasetsRepostiory
import su.pank.sprintlens.data.models.LoadingFiles
import su.pank.sprintlens.ui.screen.main.MainScreen
import kotlin.time.Duration.Companion.seconds

sealed interface SelectDatasetState{
    object Loading: SelectDatasetState

    data class Success(val datasets: List<Dataset>): SelectDatasetState

    data class Error(val throwable: Throwable): SelectDatasetState

}

class SelectDatasetScreenModel(private val datasetsRepostiory: DatasetsRepostiory): ScreenModel{

    private val datasets = datasetsRepostiory.dataSets.shareIn(screenModelScope, SharingStarted.Lazily, 1)
    val state = datasets.map { SelectDatasetState.Success(it.map { Dataset(it.id) }) }

    var isChooseNewDataset by mutableStateOf(false)

    var isFileLoading: Boolean by mutableStateOf(false)

    fun onChoosedDataset(dataset: Dataset, navigator: Navigator){
        screenModelScope.launch {
            val datasetDto = datasets.first().first { it.id == dataset.name }
            navigator.push(MainScreen(datasetDto))
        }

    }

    fun loadFiles(loadingFiles: LoadingFiles, navigator: Navigator){
        isFileLoading = true
        screenModelScope.launch {
            val datasetDTO = datasetsRepostiory.sendNewDataSet(loadingFiles)
            navigator.push(MainScreen(datasetDTO))

        }
    }

}