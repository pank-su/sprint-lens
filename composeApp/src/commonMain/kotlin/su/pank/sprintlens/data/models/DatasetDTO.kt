package su.pank.sprintlens.data.models

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class DatasetDTO(
    val id: String,
    val loadTime: LocalDateTime,
    val parsingTime: LocalTime,
    val from: LocalDateTime,
    val to: LocalDateTime,
    val teams: List<String>? = null,
    val sprintsIds: List<Long>? = null,
    val sprints: List<String>? = null
)

