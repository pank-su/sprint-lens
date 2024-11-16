package su.pank.sprintlens.data

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import su.pank.sprintlens.data.models.DatasetDTO
import su.pank.sprintlens.data.models.LoadingFiles
import javax.xml.crypto.Data
import kotlin.time.Duration.Companion.seconds


class RemoteDatasetsRepository(private val client: HttpClient) : DatasetsRepostiory {
    override val dataSets: Flow<List<DatasetDTO>> = flow<List<DatasetDTO>> {
        emit(client.get("api/DataLoad/GetDatasets").body())
    }.retry(10) {
        delay(10.seconds)
        true
    }

    override suspend fun sendNewDataSet(loadingFiles: LoadingFiles): DatasetDTO {
        val sprintFileBytes = loadingFiles.sprintFile.readBytes()
        val ticketFileBytes = loadingFiles.ticketFile.readBytes()
        val ticketHistoryFileBytes = loadingFiles.ticketFile.readBytes()

        return client.post("api/DataLoad/UploadFile") {
            setBody(MultiPartFormDataContent(formData {
                append("sprintFile", sprintFileBytes,  Headers.build {
                    append(HttpHeaders.ContentType, "text/csv")
                    append(HttpHeaders.ContentDisposition, "filename=\"sprintFile.csv\"")
                })
                append("ticketFile", ticketFileBytes,  Headers.build {
                    append(HttpHeaders.ContentType, "text/csv")
                    append(HttpHeaders.ContentDisposition, "filename=\"ticketFile.csv\"")
                })

                append("ticketHistoryFile", ticketHistoryFileBytes, Headers.build {
                    append(HttpHeaders.ContentType, "text/csv")
                    append(HttpHeaders.ContentDisposition, "filename=\"ticketHistoryFile.csv\"")
                })
            }))
        }.body<DatasetDTO>()
    }

}