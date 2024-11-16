package su.pank.sprintlens.ui.screen.main

import androidx.compose.runtime.mutableStateListOf
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import su.pank.sprintlens.data.models.DatasetDTO

class MainScreenModel(val datasetDTO: DatasetDTO): ScreenModel{
    val teams = datasetDTO.teams

    val sprints = datasetDTO.sprints

    private val _selectedTeams = MutableStateFlow(listOf<String>(teams!!.first()))

    val selectedTeams = _selectedTeams.asStateFlow()

    private val _selectedSprint = MutableStateFlow<String>(sprints!!.first())

    val selectedSprint = _selectedSprint.asStateFlow()

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

    }


}