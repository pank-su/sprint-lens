package su.pank.sprintlens.ui.screen.main

import androidx.compose.runtime.mutableStateListOf
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


sealed interface DashboardState{
    object Loading: DashboardState

    data class Successful(val analyze: SprintAnalyze): DashboardState

    object Error: DashboardState
}

class MainScreenModel(val datasetDTO: DatasetDTO, private val sprintAnalyzeRepository: SprintAnalyzeRepository): ScreenModel{
    val teams = datasetDTO.teams

    val sprints = datasetDTO.sprints

    private val _selectedTeams = MutableStateFlow(listOf<String>(teams!!.first()))

    val selectedTeams = _selectedTeams.asStateFlow()

    private val _selectedSprint = MutableStateFlow<String>(sprints!!.first())

    val selectedSprint = _selectedSprint.asStateFlow()



    private val _dashBoardState = MutableStateFlow<DashboardState>(DashboardState.Loading)
    val dashBoardState = _dashBoardState.asStateFlow()

    fun addToSelectedTeam(team:String){
        _selectedTeams.value += team
    }


    fun removeFromSelectedTeam(team: String){
        _selectedTeams.value -= team
    }

    fun selectSprint(sprint: String){
        _selectedSprint.value = sprint
    }




    init {

        screenModelScope.launch {
            selectedSprint.combine(selectedTeams){ sprint, teams ->
                val request = SprintAnalyzeRequest(datasetDTO.id, listOf(datasetDTO.sprintsIds!![sprints?.indexOfFirst { it == sprint }?:0]), teams)
                sprintAnalyzeRepository.getAnalyzeData(request)
            }.collect{}
        }
    }



}