package fr.uha.hassenforder.easy.cyclewall.ui.cycle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.hassenforder.easy.android.ui.DecoratorProperties
import fr.uha.hassenforder.easy.android.ui.app.AppTopBar
import fr.uha.hassenforder.easy.android.ui.app.appMenuEntriesBuilder
import fr.uha.hassenforder.easy.android.ui.field.group.Orientation
import fr.uha.hassenforder.easy.android.ui.field.group.OutlinedEnumRadioGroup
import fr.uha.hassenforder.easy.android.ui.field.group.RadioProperties
import fr.uha.hassenforder.easy.android.ui.field.management.managementListModeMenuEntries
import fr.uha.hassenforder.easy.android.ui.lazy.SwipeableItem
import fr.uha.hassenforder.easy.android.ui.screen.StateScreen
import fr.uha.hassenforder.easy.cyclewall.R
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao
import fr.uha.hassenforder.easy.cyclewall.icons.outlined.PhotoFrame
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import kotlinx.coroutines.Job

@Composable
fun ListCyclesScreen (
    vm : ListCyclesViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val defaultDocumentName = stringResource(id = R.string.title_cycles)

    LaunchedEffect(Unit) {
        vm.titleBuilder.setScreenNameId(sid = R.string.title_list)
        vm.titleBuilder.setDefaultDocumentName(defaultDocumentName)
    }

    StateScreen(uiState) {
        SuccessListScreen(it, vm::send )
    }
}

@Composable
fun SuccessListScreen(
    uiState: ListCyclesViewModel.UIState,
    send: (ListCyclesViewModel.UIEvent) -> Unit
) {
    val menuEntries = managementListModeMenuEntries(
        uiState.listMode,
        { send(ListCyclesViewModel.UIEvent.OnList(it)) }
    )
    Scaffold(
        topBar = {
            AppTopBar(
                uiState.uiTitle,
                { send(ListCyclesViewModel.UIEvent.OnBack) },
                menuEntries
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { send(ListCyclesViewModel.UIEvent.OnCreate) }) {
                Icon(Icons.Filled.Add, contentDescription = "create")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            OutlinedEnumRadioGroup(
                value = uiState.screenFilter,
                onValueChange = {
                    send(
                        ListCyclesViewModel.UIEvent.OnFilter(
                            CycleDao.ScreenFilter.valueOf(
                                it
                            )
                        )
                    )
                },
                properties = RadioProperties(
                    itemValues = CycleDao.ScreenFilter.entries.toTypedArray(),
                    orientation = Orientation.HorizontalList,
                    itemLabels = stringArrayResource(R.array.screen_filter)
                ),
                modifier = Modifier.fillMaxWidth(),
                decorator = DecoratorProperties(
                    labelId = R.string.hint_filter,
                ),
            )
            LazyColumn (
            ) {
                items(
                    items = uiState.cycles,
                    key = { cycle -> cycle.cid }
                ) {
                    SwipeableItem(
                        onEdit = { send(ListCyclesViewModel.UIEvent.OnEdit(it)) },
                        onDelete = { send(ListCyclesViewModel.UIEvent.OnDelete(it)) },
                    ) {
                        CycleItem(it)
                    }
                }
            }
        }
    }
}

@Composable
fun CycleItem(cycle: Cycle) {
    Card (
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = cycle.wallpaper,
                contentDescription = null,
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
                    .background(Color.Black.copy(alpha = 0.2F))
                    .padding(4.dp)
            ) {
                Text(cycle.title, color= Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                CycleTiming(cycle)
                CycleOnScreen(cycle)
            }
        }
    }
}