package su.pank.sprintlens.data.analyze

import su.pank.sprintlens.data.models.SprintAnalyze
import su.pank.sprintlens.data.models.SprintAnalyzeRequest

interface SprintAnalyzeRepository {
    suspend fun getAnalyzeData(sprintAnalyzeRequest: SprintAnalyzeRequest): List<SprintAnalyze>
}