package fr.uha.hassenforder.easy.cyclewall.ui.cycle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.HourglassBottom
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.FilterFrames
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.hassenforder.easy.android.database.model.ListMode
import fr.uha.hassenforder.easy.android.ui.DecoratorProperties
import fr.uha.hassenforder.easy.android.ui.app.AppMenuEntry
import fr.uha.hassenforder.easy.android.ui.field.management.ManagementStateText
import fr.uha.hassenforder.easy.android.ui.app.AppTopBar
import fr.uha.hassenforder.easy.android.ui.app.appMenuEntriesBuilder
import fr.uha.hassenforder.easy.android.ui.field.group.Orientation
import fr.uha.hassenforder.easy.android.ui.field.group.OutlinedEnumRadioGroup
import fr.uha.hassenforder.easy.android.ui.field.group.RadioProperties
import fr.uha.hassenforder.easy.android.ui.lazy.SwipeableItem
import fr.uha.hassenforder.easy.android.ui.field.management.managementListModeMenuEntries
import fr.uha.hassenforder.easy.android.ui.screen.StateScreen
import fr.uha.hassenforder.easy.cyclewall.R
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao
import fr.uha.hassenforder.easy.cyclewall.icons.outlined.PhotoFrame
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.ui.Converter
import fr.uha.hassenforder.easy.cyclewall.ui.cycle.ListCyclesViewModel.UIState


@Composable
fun ListCyclesScreen(
    vm: ListCyclesViewModel = hiltViewModel(),
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

enum class ItemMode {
    ListItem,
    Card,
}

@Composable
private fun SuccessListScreen(
    uiState: UIState,
    send : (ListCyclesViewModel.UIEvent) -> Unit,
) {
    val listModeEntries = managementListModeMenuEntries(
        uiState.listMode,
        { send(ListCyclesViewModel.UIEvent.OnList(it)) }
    )
    val itemMode = remember { mutableStateOf (ItemMode.ListItem) }
    val itemModeEntries = appMenuEntriesBuilder(
        selected = itemMode.value,
        itemValues = ItemMode.entries.toTypedArray(),
        itemIcons = arrayOf (Icons.Outlined.PhotoFrame, Icons.Outlined.Image),
        onChange = { itemMode.value = it }
    )
    val menuEntries = listModeEntries + itemModeEntries
    Scaffold(
        topBar = { AppTopBar(uiState.uiTitle, { send(ListCyclesViewModel.UIEvent.OnBack) }, menuEntries ) },
        floatingActionButton = {
            FloatingActionButton(onClick = { send(ListCyclesViewModel.UIEvent.OnCreate) } ) {
                Icon(Icons.Filled.Add, contentDescription = "create")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            OutlinedEnumRadioGroup(
                value = uiState.screenFilter,
                onValueChange = { send(ListCyclesViewModel.UIEvent.OnFilter(CycleDao.ScreenFilter.valueOf(it))) },
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
            LazyColumn {
                items(
                    items = uiState.cycles,
                    key = { cycle -> cycle.cid }
                ) { item ->
                    SwipeableItem(
                        onEdit = { send(ListCyclesViewModel.UIEvent.OnEdit(item)) },
                        onDelete = { send(ListCyclesViewModel.UIEvent.OnDelete(item)) }
                    ) {
                        when (itemMode.value) {
                        ItemMode.ListItem -> CycleItem(item, uiState.listMode)
                        ItemMode.Card -> CycleCard(item, uiState.listMode)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CycleItem (
    cycle : Cycle,
    listMode : ListMode,
) {

    ListItem(
        modifier = Modifier.fillMaxWidth(),
        supportingContent = {
            Box (modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                    model = cycle.wallpaper,
                    contentDescription = null,
                    error = rememberVectorPainter(Icons.Outlined.PhotoFrame),
                    placeholder = rememberVectorPainter(Icons.Outlined.HourglassBottom),
                )
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp)
                        .background(Color.Black.copy(alpha = 0.2F))
                        .padding(5.dp)
                    ,
                ) {
                    CycleTiming(cycle)
                    CycleScreen(cycle)
                }
            }
        },
        content = {
            Row (
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(cycle.title, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Spacer (modifier = Modifier.weight(1F))
                ManagementStateText(cycle.managementState, listMode)
            }
        },
    )
}

@Composable
private fun CycleCard (
    cycle : Cycle,
    listMode : ListMode,
) {

    Card(
        modifier = Modifier.fillMaxWidth().padding(3.dp),
    ) {
        Box (modifier = Modifier.fillMaxWidth()) {
            AsyncImage(
                model = cycle.wallpaper,
                contentDescription = null,
                error = rememberVectorPainter(Icons.Outlined.PhotoFrame),
                placeholder = rememberVectorPainter(Icons.Outlined.HourglassBottom),
            )
            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(3.dp)
//                    .background(Color.Black.copy(alpha = 0.2F))
//                    .padding(5.dp)
                ,
            ) {
                Text(cycle.title, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White,)
                CycleTiming(cycle)
                CycleScreen(cycle)
            }
        }
    }
}

@Composable
private fun CycleTiming (
    cycle : Cycle,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringArrayResource(R.array.rhythms)[cycle.rhythm.ordinal],
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Text(
            ":",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Text(
            Converter.convert(cycle.begin, cycle.rhythm),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Text(
            "..",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        cycle.end?.let {
            Text(
                Converter.convert(it, cycle.rhythm),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
        cycle.duration?.let {
            Text(
                pluralStringResource(R.plurals.duration, it, it),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }
    }
}

@Composable
private fun CycleScreen (
    cycle : Cycle,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            stringResource(R.string.on_screen),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
        Text(
            stringArrayResource(R.array.on_screen)[cycle.screen.ordinal],
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
        )
    }
}
