package su.pank.sprintlens.data.models

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/*
public record SprintAnalyze(
	string SprintName,
	List<Metrics> Metrics
);

public record Metrics(
	int Day,
	int CreatedTickets,
	int InWorkTickets,
	int DoneTickets
);
public record SprintAnalyzeRequest(
	string DatasetId,
	List<long> Sprints,
	List<string> Teams,
	DateTime To
);
*/


@Serializable
data class SprintAnalyzeRequest(
    val datasetId: String,
    val sprints: List<Long>,
    val teams: List<String>,
    val weightUniformity: Double? = 0.15,        // Вес равномерности
    val weightRemovedPoints: Double? = 0.05,    // Вес штрафа за удаленные задачи
    val weightLateDone: Double? = 0.1,         // Вес штрафа за поздние задачи
    val weightAddedTasks: Double? = 0.1,       // Вес добавленных задач
    val weightVelocity: Double? = 0.2,         // Вес стабильности скорости
    val weightUnfinishedTasks: Double? = 0.2,  // Вес незавершенных задач
    val weightLargeTasks: Double? = 0.2,       // Вес завершения крупных задач
    val weightTransformation: Double? = 0.2    // Вес коэффициента трансформации (только для первой половины)
)


@Serializable
data class SprintAnalyze(val sprintName: String, val metrics: List<Metrics>, val from: LocalDateTime, val to: LocalDateTime)

@Serializable
data class Metrics(
    val day: Int,
    val createdTicketPoints: Int, // Созданные
    val percentOfCreated: Double,
    val inWorkTicketPoints: Int, // В работе
    val percentOfInWork: Double,
    val doneTicketPoints: Int, // Готовые
    val percentOfDone: Double,
    val removeTicketPoints: Int, // Снятые
    val percentOfRemove: Double,
    val backlogchangedPercent: Double,
    val blockedTicketPoints: Int, // Заблокированные тикеты другой задачей
    // val excludedToday: Int, // Количество тикетов, удаленных из спринта за день
    val excludedTicketPoints: Int, // Сумма тикетов, удаленных из спринта за день
    val addedToday: Int, // Количество тикетов, добавленных в спринт за день
    val addedTicketPoints: Int, // Сумма тикетов, добавленных в спринт за день
    val sprintHealthPoints: Double
)