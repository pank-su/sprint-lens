package su.pank.sprintlens.ui.screen.select_dataset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.seconds

sealed interface SelectDatasetState{
    object Loading: SelectDatasetState

    data class Success(val datasets: List<Dataset>): SelectDatasetState

    data class Error(val throwable: Throwable): SelectDatasetState

}

class SelectDatasetScreenModel: ScreenModel{
    val state = flow{
        kotlinx.coroutines.delay(11.seconds)
        emit(SelectDatasetState.Success(listOf(Dataset("Dataset 1"), Dataset("Dataset 2"), Dataset("Dataset 3"))))
    }

    var isChooseNewDataset by mutableStateOf(false)

    fun onChoosedDataset(dataset: Dataset, navigator: Navigator){
        // TODO: do smth
    }

}