package su.pank.sprintlens.ui.screen.main

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import su.pank.sprintlens.data.analyze.RemoteSprintAnalyzeRepository
import su.pank.sprintlens.data.analyze.SprintAnalyzeRepository
import su.pank.sprintlens.data.models.DatasetDTO
import su.pank.sprintlens.data.models.SprintAnalyze
import su.pank.sprintlens.data.models.SprintAnalyzeRequest


sealed interface DashboardState {
    object Loading : DashboardState

    data class Successful(val analyze: SprintAnalyze, val selectedDay: Int) : DashboardState

    object Error : DashboardState
}

class MainScreenModel(val datasetDTO: DatasetDTO, private val sprintAnalyzeRepository: SprintAnalyzeRepository) :
    ScreenModel {


    val weightUniformity = mutableStateOf(0.15f)
    val weightRemovedPoints = mutableStateOf(0.05f)
    val weightLateDone = mutableStateOf(0.1f)
    val weightAddedTasks = mutableStateOf(0.1f)
    val weightVelocity = mutableStateOf(0.2f)
    val weightUnfinishedTasks = mutableStateOf(0.2f)
    val weightLargeTasks = mutableStateOf(0.2f)
    val weightTransformation = mutableStateOf(0.2f)

    val teams = datasetDTO.teams

    val sprints = datasetDTO.sprints

    private val _selectedTeams = MutableStateFlow(listOf<String>(teams!!.first()))

    val selectedTeams = _selectedTeams.asStateFlow()

    private val _selectedSprint = MutableStateFlow<String>(sprints!!.first())

    val selectedSprint = _selectedSprint.asStateFlow()


    private val _dashBoardState = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val dashBoardState = _dashBoardState.asStateFlow()

    fun addToSelectedTeam(team: String) {
        _selectedTeams.value += team
    }


    fun removeFromSelectedTeam(team: String) {
        if (_selectedTeams.value.size > 1)
            _selectedTeams.value -= team
    }

    fun selectSprint(sprint: String) {
        _selectedSprint.value = sprint
    }

    fun selectDay(day: Int) {
        _dashBoardState.value = (_dashBoardState.value as DashboardState.Successful).copy(selectedDay = day)
    }

    fun updateWeights(){

        addToSelectedTeam(teams?.first()!!)
        removeFromSelectedTeam(teams?.first()!!)

    }


    init {
        screenModelScope.launch {
            selectedSprint.combine(selectedTeams) { sprint, teams ->
                val teams = teams.toSet().toList()
                val request = SprintAnalyzeRequest(
                    datasetDTO.id,
                    listOf(datasetDTO.sprintsIds!![sprints?.indexOfFirst { it == sprint } ?: 0]),
                    teams,
                    weightUniformity.value.toDouble(),
                    weightRemovedPoints.value.toDouble(),
                    weightLateDone.value.toDouble(),
                    weightAddedTasks.value.toDouble(),
                    weightVelocity.value.toDouble(),
                    weightUnfinishedTasks.value.toDouble(),
                    weightLargeTasks.value.toDouble(),
                    weightTransformation.value.toDouble()


                )
                sprintAnalyzeRepository.getAnalyzeData(request)
            }.collect {
                var day: Int? = null
                if (_dashBoardState.value is DashboardState.Successful) {
                    day = (_dashBoardState.value as DashboardState.Successful).selectedDay
                }
                _dashBoardState.value = DashboardState.Successful(it.first(), day ?: it.first().metrics.first().day)
            }
        }
    }


}