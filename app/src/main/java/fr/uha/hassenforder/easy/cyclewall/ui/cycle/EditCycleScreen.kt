package fr.uha.hassenforder.easy.cyclewall.ui.cycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.hassenforder.easy.android.ui.screen.StateScreen
import fr.uha.hassenforder.easy.cyclewall.R

@Composable
fun EditCycleScreen (
    vm: CycleViewModel = hiltViewModel(),
    cid : Long
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val defaultDocumentName = stringResource(id = R.string.title_cycle)

    LaunchedEffect(Unit) {
        vm.uiTitleBuilder.setScreenNameId(sid = R.string.title_edit)
        vm.uiTitleBuilder.setDefaultDocumentName(defaultDocumentName)
        vm.edit(cid)
    }

    StateScreen(state = uiState) {
        SuccessCycleScreen(it, vm::send)
    }
}
