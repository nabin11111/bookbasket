package com.chetan.orderdelivery.presentation.admin.history

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import com.chetan.orderdelivery.R
import com.chetan.orderdelivery.common.Constants
import com.chetan.orderdelivery.presentation.common.components.LoadLottieAnimation
import com.chetan.orderdelivery.presentation.common.components.dialogs.MessageDialog
import com.chetan.orderdelivery.presentation.user.dashboard.cart.UserCartEvent
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.shape.shader.fromBrush
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.DefaultAlpha
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.component.shape.shader.DynamicShaders
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminHistoryScreen(
    nav: NavHostController,
    state: AdminHistoryState,
    event: (event: AdminHistoryEvent) -> Unit
) {
    val modelProducer = remember {
        ChartEntryModelProducer()
    }
    val datasetForModel = remember {
        mutableStateListOf(listOf<FloatEntry>())
    }
    val datasetLineSpec = remember {
        arrayListOf<LineChart.LineSpec>()
    }
    val scrollState = rememberChartScrollState()

    var isShowAlertDialog by remember {
        mutableStateOf(false)
    }
    if (isShowAlertDialog) {
        Dialog(onDismissRequest = {}) {
            Column(
                Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Delete History ",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
                LoadLottieAnimation(
                    modifier = Modifier.size(200.dp), image = R.raw.delete
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Are you sure want to delete history(ies)?",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.outline)
                )
                Spacer(modifier = Modifier.height(34.dp))

                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                        .also { Arrangement.Center }) {
                    Button(
                        modifier = Modifier.weight(1f), onClick = {
                            event(AdminHistoryEvent.DeleteHistory)
                            isShowAlertDialog = false
                        }, colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.error)
                    ) {
                        Text(text = "Yes")
                    }
                    Button(
                        modifier = Modifier.weight(1f), onClick = {
                            isShowAlertDialog = false
                        }, colors = ButtonDefaults.buttonColors(Constants.dark_primaryContainer)
                    ) {
                        Text(text = "No")
                    }
                }

            }
        }
    }


    LaunchedEffect(key1 = state.listByGroup, block = {
        datasetForModel.clear()
        datasetLineSpec.clear()
        var xPos = 0f
        val dataPoints = arrayListOf<FloatEntry>()

        datasetLineSpec.add(
            LineChart.LineSpec(
                lineColor = Green.toArgb(),
                lineBackgroundShader = DynamicShaders.fromBrush(
                    brush = Brush.verticalGradient(
                        listOf(
                            Green.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_START),
                            Green.copy(DefaultAlpha.LINE_BACKGROUND_SHADER_END)
                        )
                    )
                )
            )
        )

        state.listByGroup.forEach { groupList ->
            dataPoints.add(FloatEntry(x = xPos, groupList.second.size.toFloat()))
            xPos += 1f
        }
        datasetForModel.add(dataPoints)
        modelProducer.setEntries(datasetForModel)
    })
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.padding(horizontal = 5.dp),
                title = {
                    Text(
                        text = "Histories",
                        style = MaterialTheme.typography.titleMedium.copy(color = MaterialTheme.colorScheme.primary)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        nav.popBackStack()
                    }) {
                        Icon(
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier,
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = ""
                        )
                    }
                },
//                actions = {
//                    IconButton(onClick = {
//                        isShowAlertDialog = true
//                    }) {
//                        Icon(
//                            tint = MaterialTheme.colorScheme.error,
//                            modifier = Modifier,
//                            imageVector = Icons.Default.Delete,
//                            contentDescription = ""
//                        )
//                    }
//                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
                    .padding(horizontal = 5.dp)
            ) {
                state.infoMsg?.let {
                    MessageDialog(message = it, onDismissRequest = {
                        if (event != null && state.infoMsg.isCancellable == true) {
                            event(AdminHistoryEvent.DismissInfoMsg)
                        }
                    }, onPositive = { /*TODO*/ }) {

                    }
                }
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    if (datasetForModel.isNotEmpty() && state.listByGroup.isNotEmpty()) {
                        ProvideChartStyle {
                            Chart(
                                chart = lineChart(lines = datasetLineSpec),
                                chartModelProducer = modelProducer,
                                isZoomEnabled = true,
                                chartScrollState = scrollState,
                                startAxis = rememberStartAxis(
                                    title = "Top values",
                                    tickLength = 0.dp,
                                    valueFormatter = { value, _ ->
                                        value.toInt().toString()
                                    },
                                    itemPlacer = AxisItemPlacer.Vertical.default(
                                        maxItemCount = 6
                                    ),
                                    guideline = null
                                ),
                                bottomAxis = rememberBottomAxis(
                                    title = "Count of values",
                                    tickLength = 0.dp,
                                    valueFormatter = { value, _ ->
                                        state.listByGroup[value.toInt()].first
                                    },
                                    guideline = null
                                ),
                                marker = rememberMarker()
                            )
                        }
                    }
                }
            }


        })
}