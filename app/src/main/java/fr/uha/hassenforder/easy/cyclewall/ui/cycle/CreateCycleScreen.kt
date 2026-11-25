package fr.uha.hassenforder.easy.cyclewall.ui.cycle

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.hassenforder.easy.android.database.model.ManagementState
import fr.uha.hassenforder.easy.android.ui.screen.StateScreen
import fr.uha.hassenforder.easy.cyclewall.R
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.model.CycleRhythm
import fr.uha.hassenforder.easy.cyclewall.model.CycleScreen
import java.time.LocalDateTime

@Composable
fun CreateCycleScreen (
    vm: CycleViewModel = hiltViewModel(),
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val defaultDocumentName = stringResource(id = R.string.title_cycle)

    LaunchedEffect(Unit) {
        vm.uiTitleBuilder.setScreenNameId(sid = R.string.title_create)
        vm.uiTitleBuilder.setDefaultDocumentName(defaultDocumentName)
        val cycle = Cycle (0,
            ManagementState.CREATED,
            "no name",
            CycleScreen.SYSTEM_LOCK,
            CycleRhythm.NO,
            LocalDateTime.now(),
            null,
            1,
            null
        )
        vm.create(cycle)
    }

    StateScreen(state = uiState) {
        SuccessCycleScreen(it, vm::send )
    }

}
