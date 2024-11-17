package su.pank.sprintlens.data.analyze

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.utils.EmptyContent.contentType
import io.ktor.http.*
import su.pank.sprintlens.data.models.SprintAnalyze
import su.pank.sprintlens.data.models.SprintAnalyzeRequest

class RemoteSprintAnalyzeRepository(private val httpClient: HttpClient): SprintAnalyzeRepository {
    override suspend fun getAnalyzeData(sprintAnalyzeRequest: SprintAnalyzeRequest): List<SprintAnalyze> =
        httpClient.post("api/SprintAnalyze/GetSprintAnalyze"){
            contentType(ContentType.Application.Json)
            setBody(sprintAnalyzeRequest)
        }.body()
}