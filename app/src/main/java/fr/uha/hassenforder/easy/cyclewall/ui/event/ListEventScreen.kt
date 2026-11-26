package fr.uha.hassenforder.easy.cyclewall.ui.event

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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.HourglassBottom
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.FilterFrames
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.hassenforder.easy.android.ui.DecoratorProperties
import fr.uha.hassenforder.easy.android.ui.app.AppMenuEntry
import fr.uha.hassenforder.easy.android.ui.app.AppTopBar
import fr.uha.hassenforder.easy.android.ui.field.group.Orientation
import fr.uha.hassenforder.easy.android.ui.field.group.OutlinedEnumRadioGroup
import fr.uha.hassenforder.easy.android.ui.field.group.RadioProperties
import fr.uha.hassenforder.easy.android.ui.field.management.managementListModeMenuEntries
import fr.uha.hassenforder.easy.android.ui.screen.StateScreen
import fr.uha.hassenforder.easy.cyclewall.R
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao
import fr.uha.hassenforder.easy.cyclewall.dto.FullEvent
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.ui.Converter
import fr.uha.hassenforder.easy.cyclewall.ui.cycle.ListCyclesViewModel
import fr.uha.hassenforder.easy.cyclewall.ui.event.ListEventViewModel.UIState
import fr.uha.hassenforder.easy.android.ui.field.date.ActionOneDayLess
import fr.uha.hassenforder.easy.android.ui.field.date.ActionOneDayMore
import fr.uha.hassenforder.easy.android.ui.field.date.ActionOneMonthLess
import fr.uha.hassenforder.easy.android.ui.field.date.ActionOneMonthMore
import fr.uha.hassenforder.easy.android.ui.field.date.ActionOneWeekLess
import fr.uha.hassenforder.easy.android.ui.field.date.ActionOneWeekMore
import fr.uha.hassenforder.easy.android.ui.field.date.DateTimeFieldProperties
import fr.uha.hassenforder.easy.android.ui.field.date.OutlinedLocalDateTimeField
import fr.uha.hassenforder.easy.cyclewall.icons.outlined.PhotoFrame
import fr.uha.hassenforder.easy.cyclewall.ui.cycle.CycleOnScreen
import fr.uha.hassenforder.easy.cyclewall.ui.field.EventDayRange
import fr.uha.hassenforder.easy.cyclewall.ui.field.EventDecoratedTitle
import java.time.LocalDateTime

@Composable
fun ListEventScreen(
    vm: ListEventViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val defaultDocumentName = stringResource(id = R.string.title_events)

    LaunchedEffect(Unit) {
        vm.titleBuilder.setScreenNameId(sid = R.string.title_list)
        vm.titleBuilder.setDefaultDocumentName(defaultDocumentName)
    }

    StateScreen(uiState) {
        SuccessListScreen(it, vm::send)
    }
}

@Composable
private fun SuccessListScreen(
    uiState: UIState,
    send : (ListEventViewModel.UIEvent) -> Unit,
) {
    val listModeEntry = managementListModeMenuEntries(
        uiState.listMode,
        { send(ListEventViewModel.UIEvent.OnList(it)) }
    )
    val refreshAllEntry = listOf (
        AppMenuEntry.ActionEntry(
            icon = Icons.Outlined.Refresh,
            listener = { send(ListEventViewModel.UIEvent.OnRefreshAll(uiState.events, uiState.now)) }
        ),
    )
    val menuEntries = listModeEntry + refreshAllEntry
    Scaffold(
        topBar = { AppTopBar(uiState.uiTitle, { send(ListEventViewModel.UIEvent.OnBack) }, menuEntries ) },
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            uiState.remainingTime?.let {
                Row (
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text (stringResource(R.string.next_day))
                    Spacer(modifier = Modifier.weight(1F))
                    val nextTime = uiState.now.plusSeconds(it)
                    Text(Converter.convert(nextTime))
                }
            }
            OutlinedLocalDateTimeField(
                value = uiState.now,
                onValueChange = { it?.let { send(ListEventViewModel.UIEvent.OnNowChanged(it)) } },
                properties = DateTimeFieldProperties(
                    converter = null,
                    isNullable = false,
                    dateOnly = true
                ),
                modifier = Modifier.fillMaxWidth(),
                decorator = DecoratorProperties(
                    labelId = R.string.now,
                    hintId = R.string.hint_now
                ),
                leading = {
                    ActionOneMonthLess()
                    ActionOneWeekLess()
                    ActionOneDayLess()
                },
                trailing = {
                    ActionOneDayMore()
                    ActionOneWeekMore()
                    ActionOneMonthMore()
                }
            )
            OutlinedEnumRadioGroup(
                value = uiState.screenFilter,
                onValueChange = { send(ListEventViewModel.UIEvent.OnFilter(CycleDao.ScreenFilter.valueOf(it))) },
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
                    items = uiState.events,
                    key = { next -> next.event.eid }
                ) { item ->
                    if (item.cycle == null) NoCycleEventItem(uiState, item, send)
                    else EventItem(uiState, item, send)
                }
            }
        }
    }
}

@Composable
private fun NoCycleEventItem (
    uiState : UIState,
    fullEvent : FullEvent,
    send : (ListEventViewModel.UIEvent) -> Unit,
) {
    ListItem(
        modifier = Modifier.fillMaxWidth(),
        trailingContent = {
            IconButton({ send(ListEventViewModel.UIEvent.OnDelete(fullEvent)) }) {
                Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription = "",
                )
            }
        },
        content = {
            Text(stringResource(R.string.event_without_cycle), color = Color.Red, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        },
    )
}

@Composable
private fun EventItem (
    uiState : UIState,
    fullEvent : FullEvent,
    send : (ListEventViewModel.UIEvent) -> Unit,
) {
    Card (
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
    ) {
        val cycle = fullEvent.cycle!!
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
                EventDecoratedTitle(cycle, fullEvent == uiState.current)
                EventDayRange(fullEvent.event.begin, fullEvent.event.end)
                CycleOnScreen(cycle)
            }
        }
    }
}
