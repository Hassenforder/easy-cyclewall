package fr.uha.hassenforder.easy.cyclewall.ui.settings

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.WorkInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.easy.android.flow.Result
import fr.uha.hassenforder.easy.android.navigation.Navigator
import fr.uha.hassenforder.easy.cyclewall.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor (
    private val repository: SettingsRepository,
    private val navigator: Navigator,
): ViewModel() {

    private val _jobsInfo = repository.jobsInfo

    @Immutable
    data class UIState (
        val jobsInfo : List<WorkInfo>,
    ) {
        companion object {
            fun create(jobsInfo : List<WorkInfo>): UIState {
                return UIState(jobsInfo)
            }
        }
    }

    val uiState : StateFlow<Result<UIState>> = _jobsInfo.map() { j ->
        Result.Success(UIState.create(j))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Result.Loading()
    )

    sealed class UIEvent {
        data object OnUp: UIEvent()
        data object OnDatabaseClear: UIEvent()
        data class OnDatabaseFeed(val mode : Int, val nextAlso : Boolean): UIEvent()
        data object OnJobStart: UIEvent()
        data object OnJobCancel: UIEvent()
        data object OnCancelAll: UIEvent()
    }

    fun send (uiEvent : UIEvent) = viewModelScope.launch {
        when (uiEvent) {
            is UIEvent.OnUp -> navigator.navigateUp()
            UIEvent.OnDatabaseClear -> repository.clearDatabase()
            is UIEvent.OnDatabaseFeed -> repository.populateDatabase (uiEvent.mode, uiEvent.nextAlso)
            UIEvent.OnJobStart -> repository.startJob ()
            UIEvent.OnJobCancel -> repository.cancelJob ()
            UIEvent.OnCancelAll -> repository.cancelAll ()
        }
    }

}