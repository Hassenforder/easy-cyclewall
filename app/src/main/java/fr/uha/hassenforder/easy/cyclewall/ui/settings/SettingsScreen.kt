package fr.uha.hassenforder.easy.cyclewall.ui.settings

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import fr.uha.hassenforder.easy.android.ui.app.AppTopBar
import fr.uha.hassenforder.easy.android.ui.app.UITitle
import fr.uha.hassenforder.easy.android.ui.screen.MetaDataScreen
import fr.uha.hassenforder.easy.android.ui.screen.StateScreen
import fr.uha.hassenforder.easy.cyclewall.R

@Composable
fun SettingsScreen(
    vm: SettingsViewModel = hiltViewModel(),
) {
    val uiState = vm.uiState.collectAsState()

    StateScreen(uiState.value) {
        InternalSettingsScreen(it, vm::send)
    }

}

@Composable
private fun InternalSettingsScreen(
    uiState: SettingsViewModel.UIState,
    send : (SettingsViewModel.UIEvent) -> Unit,
) {
    Scaffold(
        topBar = { AppTopBar(UITitle(screenNameId = R.string.title_settings)) },
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            DatabaseFields(uiState, send)
            WorkerFields(uiState, send)
            MetaDataScreen(initiallyOpened = false, modifier = Modifier.fillMaxSize())
        }
    }

}

@Composable
private fun DatabaseFields (
    uiState: SettingsViewModel.UIState,
    send : (SettingsViewModel.UIEvent) -> Unit,
) {
    Button(onClick = { send(SettingsViewModel.UIEvent.OnDatabaseClear) }) {
        Text(stringResource(id = R.string.clear))
    }
    Text(stringResource(id = R.string.populate))
    val nextAlso = remember { mutableStateOf<Boolean>(true) }
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(stringResource(id = R.string.nextAlso))
        Spacer(Modifier.weight(1f))
        Switch(nextAlso.value,
            onCheckedChange = {nextAlso.value = it}
        )
    }
    Text(stringResource(id = R.string.drawings))
    Row {
        Button(onClick = { send(SettingsViewModel.UIEvent.OnDatabaseFeed(1, nextAlso.value)) }) {
            Text(stringResource(id = R.string.populate_seasons))
        }
        Button(onClick = { send(SettingsViewModel.UIEvent.OnDatabaseFeed(2, nextAlso.value)) }) {
            Text(stringResource(id = R.string.populate_christmas))
        }
        Button(onClick = { send(SettingsViewModel.UIEvent.OnDatabaseFeed(3, nextAlso.value)) }) {
            Text(stringResource(id = R.string.populate_year))
        }
    }
    Text(stringResource(id = R.string.paintings))
    Row {
        Button(onClick = { send(SettingsViewModel.UIEvent.OnDatabaseFeed(11, nextAlso.value)) }) {
            Text(stringResource(id = R.string.populate_seasons))
        }
        Button(onClick = { send(SettingsViewModel.UIEvent.OnDatabaseFeed(12, nextAlso.value)) }) {
            Text(stringResource(id = R.string.populate_christmas))
        }
        Button(onClick = { send(SettingsViewModel.UIEvent.OnDatabaseFeed(13, nextAlso.value)) }) {
            Text(stringResource(id = R.string.populate_year))
        }
    }
    Text(stringResource(id = R.string.specials))
    Row {
        Button(onClick = { send(SettingsViewModel.UIEvent.OnDatabaseFeed(21, nextAlso.value)) }) {
            Text(stringResource(id = R.string.populate_months1))
        }
        Button(onClick = { send(SettingsViewModel.UIEvent.OnDatabaseFeed(22, nextAlso.value)) }) {
            Text(stringResource(id = R.string.populate_months2))
        }
        Button(onClick = { send(SettingsViewModel.UIEvent.OnDatabaseFeed(23, nextAlso.value)) }) {
            Text(stringResource(id = R.string.populate_months3))
        }
    }
}

@Composable
private fun WorkerFields (
    uiState: SettingsViewModel.UIState,
    send : (SettingsViewModel.UIEvent) -> Unit,
) {
    Text(stringResource(id = R.string.job), fontSize = 16.sp, fontWeight = FontWeight.Bold)
    WorkInfosField(uiState.jobsInfo)
    Row (
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Button(onClick = { send(SettingsViewModel.UIEvent.OnJobStart) }) {
            Text(stringResource(id = R.string.start))
        }
        Button(onClick = { send(SettingsViewModel.UIEvent.OnJobCancel) }) {
            Text(stringResource(id = R.string.cancel))
        }
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { send(SettingsViewModel.UIEvent.OnCancelAll) }) {
            Text(stringResource(id = R.string.cancel_all))
        }
    }
}
