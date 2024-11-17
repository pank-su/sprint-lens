package su.pank.sprintlens.ui.screen.main

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.rememberShimmer
import com.valentinilk.shimmer.shimmer
import io.github.koalaplot.core.ChartLayout
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.bar.DefaultVerticalBar
import io.github.koalaplot.core.bar.DefaultVerticalBarPlotEntry
import io.github.koalaplot.core.bar.DefaultVerticalBarPosition
import io.github.koalaplot.core.bar.VerticalBarPlot
import io.github.koalaplot.core.line.AreaBaseline
import io.github.koalaplot.core.line.AreaPlot
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.pie.PieChart
import io.github.koalaplot.core.style.AreaStyle
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.CategoryAxisModel
import io.github.koalaplot.core.xygraph.FloatLinearAxisModel
import io.github.koalaplot.core.xygraph.Point
import io.github.koalaplot.core.xygraph.XYGraph
import io.ktor.http.*
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.datetime.plus
import org.jetbrains.compose.resources.painterResource
import org.koin.core.parameter.parameterSetOf
import sprintlens.composeapp.generated.resources.Res
import sprintlens.composeapp.generated.resources.logo
import sprintlens.composeapp.generated.resources.sprint
import su.pank.sprintlens.data.models.DatasetDTO
import su.pank.sprintlens.data.models.SprintAnalyze
import su.pank.sprintlens.ui.components.Logo
import su.pank.sprintlens.ui.theme.extendedDark
import su.pank.sprintlens.ui.theme.extendedLight
import su.pank.sprintlens.ui.theme.logoTitle
import kotlin.math.min


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


    @Composable
    fun SliderWithTitle(title: String, state: MutableState<Float>) {
        var myState by remember {
            mutableStateOf(state.value)
        }
        Column(modifier = Modifier.padding(vertical = 8.dp)) {
            Text(text = "$title: ${"%.2f".format(myState)}")
            Slider(
                value = myState,
                onValueChange = { myState = it },
                onValueChangeFinished = {
                    state.value = myState
                },
                valueRange = 0f..1f, // Указываем диапазон (например, от 0 до 1)
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @Composable
    fun MetricsField(label: String, value: String) {
        Row(
            Modifier.height(52.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,

            ) {
            Spacer(Modifier.width(10.dp))
            Text(
                text = "$label: ",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(1.dp))

            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(10.dp))

        }
    }

    @OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class, ExperimentalKoalaPlotApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel: MainScreenModel = koinScreenModel(parameters = { parameterSetOf(dataset) })
        var maxLines by remember {
            mutableStateOf(1)
        }
        val selectedTeams by screenModel.selectedTeams.collectAsState()
        val selectedSprint by screenModel.selectedSprint.collectAsState()

        val dashboardState by screenModel.dashBoardState.collectAsState()

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
                                    screenModel.addToSelectedTeam(it)
                            })
                        }

                    }
                }

            }
            val shimmerInstance = rememberShimmer(shimmerBounds = ShimmerBounds.Window)
            val primary = MaterialTheme.colorScheme.primary

            val secondary = MaterialTheme.colorScheme.secondary


            // Скрытие слайдера
            Row(modifier = Modifier, horizontalArrangement = Arrangement.spacedBy(30.dp)) {

                when (dashboardState) {
                    DashboardState.Error -> Text("Что-то пошло не так")
                    DashboardState.Loading -> {
                        Box(
                            modifier = Modifier.width(294.dp).height(44.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        ) {

                        }
                        Box(
                            Modifier.shimmer(shimmerInstance).fillMaxWidth().height(44.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        )
                    }

                    is DashboardState.Successful -> {
                        val data = (dashboardState as DashboardState.Successful).analyze
                        val day = (dashboardState as DashboardState.Successful).selectedDay

                        val text by remember(day) {
                            derivedStateOf {
                                val format = LocalDate.Format {
                                    dayOfMonth()
                                    chars("/")
                                    monthNumber()
                                }


                                data.from.date.plus(day, DateTimeUnit.DAY).format(format)
                            }
                        }
                        Box(
                            modifier = Modifier.width(294.dp)
                                .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                        ) {
                            Text(
                                text,
                                textAlign = TextAlign.End,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.fillMaxWidth().padding(10.dp)
                            )
                        }
                        val sliderState = remember {
                            SliderState(
                                valueRange = 1f..data.metrics.size.toFloat(),
                                steps = data.metrics.size,


                                )
                        }

                        LaunchedEffect(Unit) {
                            sliderState.onValueChangeFinished = {
                                screenModel.selectDay(sliderState.value.toInt())
                            }
                        }


                        Slider(sliderState)
                    }
                }

            }


            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(30.dp)) {

                LazyColumn(
                    modifier = Modifier.width(294.dp).fillMaxHeight()
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

                val combinedWeights = remember {
                    derivedStateOf {
                        listOf(
                            screenModel.weightUniformity.value,
                            screenModel.weightRemovedPoints.value,
                            screenModel.weightLateDone.value,
                            screenModel.weightAddedTasks.value,
                            screenModel.weightVelocity.value,
                            screenModel.weightUnfinishedTasks.value,
                            screenModel.weightLargeTasks.value,
                            screenModel.weightTransformation.value
                        )
                    }
                }

                LaunchedEffect(combinedWeights.value) {
                    screenModel.updateWeights()
                }

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(320.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    when (dashboardState) {
                        DashboardState.Error -> {}
                        DashboardState.Loading -> {
                            items(4) {
                                Box(
                                    modifier = Modifier.shimmer(shimmerInstance).fillMaxWidth().height(300.dp)
                                        .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(12.dp))
                                )
                            }
                        }

                        is DashboardState.Successful -> {
                            val data = (dashboardState as DashboardState.Successful).analyze
                            val day = (dashboardState as DashboardState.Successful).selectedDay
                            val dayMetrics = data.metrics[day - 1]


                            DoneTasksPlot(data, primary)

                            item {

                                Box(
                                    modifier = Modifier.fillMaxWidth().height(300.dp)
                                        .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(12.dp))
                                ) {
                                    FlowRow(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        val surface = MaterialTheme.colorScheme.surfaceBright


                                        val extendedColors =
                                            if (isSystemInDarkTheme()) MaterialTheme.colorScheme.extendedDark else MaterialTheme.colorScheme.extendedLight

                                        val errorContainer = MaterialTheme.colorScheme.errorContainer
                                        val errorColor = MaterialTheme.colorScheme.onErrorContainer


                                        val containerColor by remember(day) {
                                            derivedStateOf {
                                                when (dayMetrics.sprintHealthPoints) {
                                                    in 70.0..100.0 -> extendedColors.good.colorContainer
                                                    in 30.0..<70.0 -> extendedColors.middle.colorContainer
                                                    else -> errorContainer
                                                }
                                            }
                                        }

                                        val content by remember(day) {
                                            derivedStateOf {
                                                when (dayMetrics.sprintHealthPoints) {
                                                    in 70.0..100.0 -> extendedColors.good.onColorContainer
                                                    in 30.0..<70.0 -> extendedColors.middle.onColorContainer
                                                    else -> errorColor
                                                }
                                            }
                                        }

                                        //MetricsField("День", dayMetrics.day.toString())


                                        Row(
                                            Modifier.height(52.dp)
                                                .background(containerColor, RoundedCornerShape(12.dp)),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center,

                                            ) {
                                            Spacer(Modifier.width(10.dp))

                                            Box {
                                                Icon(
                                                    Icons.Filled.Favorite,
                                                    null,
                                                    Modifier.graphicsLayer(alpha = 0.99f).drawWithCache {
                                                        onDrawWithContent {
                                                            drawContent()
                                                            drawRect(
                                                                Brush.verticalGradient(
                                                                    0f to surface,
                                                                    1f - (dayMetrics.sprintHealthPoints.toFloat() / 100f - 0.01f) to containerColor,

                                                                    1f - (dayMetrics.sprintHealthPoints.toFloat() / 100f + 0.01f) to content,

                                                                    1f - (dayMetrics.sprintHealthPoints.toFloat() / 100f) to content
                                                                ),
                                                                blendMode = BlendMode.SrcAtop
                                                            )
                                                        }
                                                    })
                                                Icon(
                                                    Icons.Rounded.FavoriteBorder,
                                                    null,
                                                    Modifier
                                                )
                                            }
                                            Text(
                                                "${"%.2f".format(data.metrics[day - 1].sprintHealthPoints)} %",
                                                color = content,
                                                style = MaterialTheme.typography.bodyLarge
                                            )
                                            Spacer(Modifier.width(10.dp))

                                        }

                                        MetricsField("Очки созданных", dayMetrics.createdTicketPoints.toString())
                                        MetricsField(
                                            "Процент созданных",
                                            "%.2f%%".format(dayMetrics.percentOfCreated)
                                        )
                                        MetricsField("Очки в работе", dayMetrics.inWorkTicketPoints.toString())
                                        MetricsField(
                                            "Процент в работе",
                                            "%.2f%%".format(dayMetrics.percentOfInWork)
                                        )
                                        MetricsField("Очки завершённых", dayMetrics.doneTicketPoints.toString())


                                    }


                                }
                            }

                            item {

                                Box(
                                    modifier = Modifier.fillMaxWidth().height(300.dp)
                                        .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(12.dp))
                                ) {
                                    FlowRow(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {

                                        MetricsField("Очки завершённых", dayMetrics.doneTicketPoints.toString())
                                        MetricsField(
                                            "Процент завершённых",
                                            "%.2f%%".format(dayMetrics.percentOfDone)
                                        )
                                        MetricsField("Очки удалённых", dayMetrics.removeTicketPoints.toString())
                                        MetricsField(
                                            "Процент удалённых",
                                            "%.2f%%".format(dayMetrics.percentOfRemove)
                                        )
                                        MetricsField(
                                            "Очки заблокированных",
                                            dayMetrics.blockedTicketPoints.toString()
                                        )
                                        MetricsField("Исключённые", dayMetrics.excludedTicketPoints.toString())
                                        MetricsField("Добавленные сегодня", dayMetrics.addedToday.toString())
                                        MetricsField("Добавленные", dayMetrics.addedTicketPoints.toString())


                                    }


                                }
                            }




                            item(span = {
                                GridItemSpan(min(2, this.maxCurrentLineSpan))
                            }) {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(300.dp)
                                        .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(12.dp))
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Добавленные поинты")
                                        XYGraph(
                                            xAxisModel = CategoryAxisModel(data.metrics.map { it.day }),
                                            yAxisModel = FloatLinearAxisModel(
                                                0f..(data.metrics.map { it.createdTicketPoints }.max().toFloat() + 10f),
                                            ),
                                            horizontalMajorGridLineStyle = null,
                                            horizontalMinorGridLineStyle = null,
                                            verticalMajorGridLineStyle = null,
                                            verticalMinorGridLineStyle = null,
                                            xAxisLabels = {

                                                val format = LocalDate.Format {
                                                    dayOfMonth()
                                                    chars("/")
                                                    monthNumber()
                                                }

                                                data.from.date.plus(it, DateTimeUnit.DAY).format(format)
                                            },

                                            ) {
                                            AreaPlot<Int, Float>(
                                                data = data.metrics.map {
                                                    Point<Int, Float>(
                                                        it.day,
                                                        it.createdTicketPoints.toFloat()
                                                    )
                                                }, lineStyle = LineStyle(
                                                    brush = SolidColor(
                                                        primary
                                                    ),
                                                    strokeWidth = 2.dp
                                                ),

                                                areaStyle = AreaStyle(
                                                    brush = SolidColor(primary.copy(alpha = 0.8f)),
                                                    alpha = 0.5f,
                                                ),
                                                areaBaseline = AreaBaseline.ConstantLine(0f)
                                            )
                                        }
                                    }
                                }
                            }

                            item() {
                                Box(
                                    modifier = Modifier.fillMaxWidth().height(300.dp)
                                        .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(12.dp))
                                ) {
                                    Column(modifier = Modifier.padding(10.dp)) {
                                        Text("Выполненные к невыполненным")

                                        PieChart(
                                            values = listOf(
                                                dayMetrics.percentOfDone.toFloat() / 100f,
                                                1f - (dayMetrics.percentOfDone.toFloat() / 100f)
                                            ),
                                            label = {
                                                when (it) {
                                                    0 -> Text("% отрытых")
                                                    1 -> {
                                                        if (dayMetrics.percentOfDone.toFloat() > 1f)
                                                            Text("% закрытых")
                                                    }
                                                }
                                            }
                                        )

                                        Row {

                                        }

                                    }
                                }
                            }

                            HealthPlot(data, secondary)

                            BacklogChart(data, secondary)


                            item {

                                Box(
                                    modifier = Modifier.fillMaxWidth().height(300.dp)
                                        .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(12.dp))
                                ) {
                                    FlowRow(
                                        modifier = Modifier.padding(10.dp),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {


                                        MetricsField(
                                            "Очки заблокированных",
                                            dayMetrics.blockedTicketPoints.toString()
                                        )
                                        MetricsField("Исключённые", dayMetrics.excludedTicketPoints.toString())
                                        MetricsField("Добавленные сегодня", dayMetrics.addedToday.toString())
                                        MetricsField("Добавленные очки", dayMetrics.addedTicketPoints.toString())
                                    }


                                }
                            }
                            item {
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(12.dp))
                                ) {
                                    Text("Настройки расчёта здоровья спринта")

                                    // Определяем состояния для всех весов


                                    SliderWithTitle("Вес равномерности", screenModel.weightUniformity)
                                    SliderWithTitle("Вес штрафа за удаленные задачи", screenModel.weightRemovedPoints)
                                    SliderWithTitle("Вес штрафа за поздние задачи", screenModel.weightLateDone)
                                    SliderWithTitle("Вес добавленных задач", screenModel.weightAddedTasks)
                                    SliderWithTitle("Вес стабильности скорости", screenModel.weightVelocity)
                                    SliderWithTitle("Вес незавершенных задач", screenModel.weightUnfinishedTasks)
                                    SliderWithTitle("Вес завершения крупных задач", screenModel.weightLargeTasks)
                                    SliderWithTitle("Вес коэффициента трансформации", screenModel.weightTransformation)

                                }
                            }




                        }
                    }

                }
            }


        }
    }

    @OptIn(ExperimentalKoalaPlotApi::class)
    private fun LazyGridScope.DoneTasksPlot(
        data: SprintAnalyze,
        primary: Color
    ) {
        item(span = {
            GridItemSpan(min(2, this.maxCurrentLineSpan))
        }) {
            Box(
                modifier = Modifier.fillMaxWidth().height(300.dp)
                    .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Выполненные задачи")
                    XYGraph(
                        xAxisModel = CategoryAxisModel(data.metrics.map { it.day }),
                        yAxisModel = FloatLinearAxisModel(
                            0f..(data.metrics.map { it.doneTicketPoints }.max().toFloat() + 10f),
                        ),
                        horizontalMajorGridLineStyle = null,
                        horizontalMinorGridLineStyle = null,
                        verticalMajorGridLineStyle = null,
                        verticalMinorGridLineStyle = null,
                        xAxisLabels = {

                            val format = LocalDate.Format {
                                dayOfMonth()
                                chars("/")
                                monthNumber()
                            }

                            data.from.date.plus(it, DateTimeUnit.DAY).format(format)
                        },

                        ) {
                        AreaPlot<Int, Float>(
                            data = data.metrics.map {
                                Point<Int, Float>(
                                    it.day,
                                    it.doneTicketPoints.toFloat()
                                )
                            }, lineStyle = LineStyle(
                                brush = SolidColor(
                                    primary
                                ),
                                strokeWidth = 2.dp
                            ),

                            areaStyle = AreaStyle(
                                brush = SolidColor(primary.copy(alpha = 0.8f)),
                                alpha = 0.5f,
                            ),
                            areaBaseline = AreaBaseline.ConstantLine(0f)
                        )
                    }
                }
            }
        }
    }



    @OptIn(ExperimentalKoalaPlotApi::class)
    private fun LazyGridScope.BacklogChart(
        data: SprintAnalyze,
        primary: Color
    ) {
        item(span = {
            GridItemSpan(min(2, this.maxCurrentLineSpan))
        }) {
            Box(
                modifier = Modifier.fillMaxWidth().height(300.dp)
                    .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Изменение бэклога")
                    XYGraph(
                        xAxisModel = CategoryAxisModel(data.metrics.map { it.day }),
                        yAxisModel = FloatLinearAxisModel(
                            0f..(data.metrics.map { it.backlogchangedPercent }.max().toFloat() + 0.1f),
                        ),
                        horizontalMajorGridLineStyle = null,
                        horizontalMinorGridLineStyle = null,
                        verticalMajorGridLineStyle = null,
                        verticalMinorGridLineStyle = null,
                        xAxisLabels = {

                            val format = LocalDate.Format {
                                dayOfMonth()
                                chars("/")
                                monthNumber()
                            }

                            data.from.date.plus(it, DateTimeUnit.DAY).format(format)
                        },

                        ) {
                        AreaPlot<Int, Float>(
                            data = data.metrics.map {
                                Point<Int, Float>(
                                    it.day,
                                    it.backlogchangedPercent.toFloat()
                                )
                            }, lineStyle = LineStyle(
                                brush = SolidColor(
                                    primary
                                ),
                                strokeWidth = 2.dp
                            ),

                            areaStyle = AreaStyle(
                                brush = SolidColor(primary.copy(alpha = 0.8f)),
                                alpha = 0.5f,
                            ),
                            areaBaseline = AreaBaseline.ConstantLine(0f)
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalKoalaPlotApi::class)
    private fun LazyGridScope.HealthPlot(
        data: SprintAnalyze,
        primary: Color
    ) {
        item(span = {
            GridItemSpan(min(2, this.maxCurrentLineSpan))
        }) {
            Box(
                modifier = Modifier.fillMaxWidth().height(300.dp)
                    .background(MaterialTheme.colorScheme.surfaceBright, RoundedCornerShape(12.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text("Здоровье спринта")
                    XYGraph(
                        xAxisModel = CategoryAxisModel(data.metrics.map { it.day }),
                        yAxisModel = FloatLinearAxisModel(
                            0f..(data.metrics.map { it.sprintHealthPoints }.max().toFloat() + 10f),
                        ),
                        horizontalMajorGridLineStyle = null,
                        horizontalMinorGridLineStyle = null,
                        verticalMajorGridLineStyle = null,
                        verticalMinorGridLineStyle = null,
                        xAxisLabels = {

                            val format = LocalDate.Format {
                                dayOfMonth()
                                chars("/")
                                monthNumber()
                            }

                            data.from.date.plus(it, DateTimeUnit.DAY).format(format)
                        },

                        ) {
                        VerticalBarPlot(
                            data = data.metrics.map {
                                DefaultVerticalBarPlotEntry(
                                    it.day,
                                    DefaultVerticalBarPosition(
                                        0f, it.sprintHealthPoints.toFloat()
                                    )
                                )
                            },
                            bar = {
                                val extendedColors =
                                    if (isSystemInDarkTheme()) MaterialTheme.colorScheme.extendedDark else MaterialTheme.colorScheme.extendedLight

                                val errorContainer = MaterialTheme.colorScheme.errorContainer
                                val containerColor =

                                    when (data.metrics[it].sprintHealthPoints) {
                                        in 70.0..100.0 -> extendedColors.good.colorContainer
                                        in 30.0..<70.0 -> extendedColors.middle.colorContainer
                                        else -> errorContainer
                                    }


                                DefaultVerticalBar(
                                    brush = SolidColor(containerColor),
                                    modifier = Modifier.fillMaxWidth(),
                                ) {

                                }
                            },

                            )
                    }
                }
            }
        }
    }


}

