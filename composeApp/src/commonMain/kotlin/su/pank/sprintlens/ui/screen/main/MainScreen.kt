package su.pank.sprintlens.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.ktor.http.*
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parameterSetOf
import sprintlens.composeapp.generated.resources.Res
import sprintlens.composeapp.generated.resources.logo
import sprintlens.composeapp.generated.resources.sprint
import su.pank.sprintlens.data.models.DatasetDTO
import su.pank.sprintlens.ui.components.Logo
import su.pank.sprintlens.ui.theme.logoTitle


class MainScreen(val dataset: DatasetDTO) : Screen {

    @Composable
    fun TeamChip(name: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
        ElevatedFilterChip(
            selected, onClick, { Text(name) }, leadingIcon = if (selected) {
                {
                    Icon(Icons.Default.Check, null)
                }
            } else null
        )
    }

    @OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: MainScreenModel = koinScreenModel(parameters = { parameterSetOf(dataset) })
        var maxLines by remember {
            mutableStateOf(1)
        }
        val selectedTeams by screenModel.selectedTeams.collectAsState()
        val selectedSprint by screenModel.selectedSprint.collectAsState()
        Column(modifier = Modifier.fillMaxWidth().padding(32.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(modifier = Modifier.height(68.dp * maxLines), horizontalArrangement = Arrangement.spacedBy(30.dp)) {
                Box(
                    modifier = Modifier.size(294.dp, 68.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)).clickable {
                            navigator.pop()
                        }
                ) {
                    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.primary) {
                        Row(Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically) {
                            Spacer(Modifier.width(15.dp))
                            Icon(painterResource(Res.drawable.logo), "ecg heart", modifier = Modifier.size(44.dp))
                            Spacer(Modifier.width(10.dp))
                            Text("Sprint Lens", style = MaterialTheme.typography.logoTitle, fontSize = 36.sp)
                        }
                    }
                }

                Box(
                    modifier = Modifier.weight(1f).height(68.dp * maxLines)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                ) {

                    FlowRow(
                        maxLines = maxLines,
                        verticalArrangement = Arrangement.Top,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxHeight().padding(vertical = 9.dp, horizontal = 10.dp),
                        overflow = FlowRowOverflow.expandOrCollapseIndicator({
                            IconButton(onClick = { maxLines += 1 }) {
                                Icon(Icons.Default.ArrowDropDown, null)
                            }
                        }, {
                            IconButton(onClick = { maxLines = 1 }) {
                                Icon(Icons.Default.ArrowDropUp, null)
                            }

                        }, minRowsToShowCollapse = 2)
                    ) {

                        Text("Команды:", style = MaterialTheme.typography.headlineSmall, lineHeight = 40.sp)

                        screenModel.teams?.forEach {
                            TeamChip(it, selectedTeams.contains(it), {
                                if (selectedTeams.contains(it))
                                    screenModel.removeFromSelectedTeam(it)
                                    else
                                screenModel.addToSelectedTeam(it) })
                        }

                    }
                }

            }
            if (false){
                Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(30.dp)) {
                    Box(
                        modifier = Modifier.width(294.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                    ) {
                        Text(
                            "Временной промежуток:",
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth().padding(10.dp)
                        )
                    }
                    Slider(SliderState())
                }
            }

            Row(modifier = Modifier.weight(1f)) {

                LazyColumn(
                    modifier = Modifier.width(294.dp)
                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
                    contentPadding = PaddingValues(10.dp)
                ) {
                    items(screenModel.sprints ?: listOf()) {
                        NavigationDrawerItem(
                            label = { Text(it, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                            selected = it == selectedSprint,
                            onClick = { screenModel.selectSprint(it) }, icon = {
                                Icon(painterResource(Res.drawable.sprint), null)
                            }
                        )
                    }
                }

                LazyVerticalGrid(columns = GridCells.Adaptive(300.dp)){
                    items(2){

                    }
                }
            }


        }
    }


}

