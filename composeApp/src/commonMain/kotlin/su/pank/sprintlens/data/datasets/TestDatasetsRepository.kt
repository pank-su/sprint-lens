package su.pank.sprintlens.data

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import su.pank.sprintlens.data.models.DatasetDTO
import su.pank.sprintlens.data.models.LoadingFiles
import su.pank.sprintlens.data.models.Sprint
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.seconds

class TestDatasetsRepository: DatasetsRepostiory {

    fun createTestDataset(): DatasetDTO {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val sprint1 = Sprint(
            id = 1,
            sprintName = "Sprint 1",
            sprintStatus = "Completed",
            sprintStartDate = LocalDateTime(2024, 1, 1, 9, 0),
            sprintEndDate = LocalDateTime(2024, 1, 15, 17, 0)
        )

        val sprint2 = Sprint(
            id = 2,
            sprintName = "Sprint 2",
            sprintStatus = "In Progress",
            sprintStartDate = LocalDateTime(2024, 1, 16, 9, 0),
            sprintEndDate = LocalDateTime(2024, 1, 30, 17, 0)
        )

        return DatasetDTO(
            id = "dataset-001",
            loadTime = now,
            parsingTime =   now.time, // Минус 1 час
            from = LocalDateTime(2024, 1, 1, 0, 0),
            to = LocalDateTime(2024, 1, 30, 23, 59),
            teams = listOf("Team A", "Team B", "Team C"),
            sprintsIds = listOf(sprint1.id, sprint2.id),
            sprints = listOf("print1, sprint2")
        )
    }
    override val dataSets: Flow<List<DatasetDTO>> = flow{
        kotlinx.coroutines.delay(1.seconds)
        emit(listOf(createTestDataset()))
    }

    override suspend fun sendNewDataSet(loadingFiles: LoadingFiles): DatasetDTO {
        return createTestDataset()
    }
}
