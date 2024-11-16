package su.pank.sprintlens.data.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sprint(
    @SerialName("Id") val id: Long,
    @SerialName("SprintName") val sprintName: String,
    @SerialName("SprintStatus") val sprintStatus: String,
    @SerialName("SprintStartDate") val sprintStartDate: LocalDateTime,
    @SerialName("SprintEndDate") val sprintEndDate: LocalDateTime
)


