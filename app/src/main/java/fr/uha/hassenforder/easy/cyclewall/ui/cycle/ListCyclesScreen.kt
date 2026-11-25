package fr.uha.hassenforder.easy.cyclewall.ui.cycle

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.room.util.TableInfo
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
fun ListCyclesScreen (
    vm : ListCyclesViewModel = hiltViewModel()
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    StateScreen(uiState) {
        SuccessListScreen (it)
    }
}

@Composable
fun SuccessListScreen(uiState: ListCyclesViewModel.UIState) {
    LazyColumn() {
        items (
            items = uiState.cycles,
            key = { cycle -> cycle.cid }
        ) {
            SwipeableItem(
                onEdit = { },
                onDelete = { },
            ) {
                CycleItem (it)
            }
        }
    }
}

@Composable
fun CycleItem(cycle: Cycle) {
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
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
                    .padding(3.dp)
            ) {
                Text(cycle.title, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                CycleTiming(cycle, color = Color.White)
                CycleScreen(cycle, color = Color.White)
            }
        }
    }
}