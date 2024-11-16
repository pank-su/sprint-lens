package su.pank.sprintlens.data

import kotlinx.coroutines.flow.Flow
import su.pank.sprintlens.data.models.DatasetDTO
import su.pank.sprintlens.data.models.LoadingFiles

interface DatasetsRepostiory {

    val dataSets: Flow<List<DatasetDTO>>

    suspend fun sendNewDataSet(loadingFiles: LoadingFiles): DatasetDTO
}