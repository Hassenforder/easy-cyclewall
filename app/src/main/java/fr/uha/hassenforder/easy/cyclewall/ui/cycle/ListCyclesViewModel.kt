package fr.uha.hassenforder.easy.cyclewall.ui.cycle

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.easy.android.database.model.ListMode
import fr.uha.hassenforder.easy.android.database.model.ManagementState
import fr.uha.hassenforder.easy.android.flow.Result
import fr.uha.hassenforder.easy.android.navigation.Navigator
import fr.uha.hassenforder.easy.android.ui.app.UITitle
import fr.uha.hassenforder.easy.android.ui.app.UITitleBuilder
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao.ScreenFilter
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.navigation.CyclesDestination
import fr.uha.hassenforder.easy.cyclewall.repository.CycleRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCyclesViewModel @Inject constructor (
    private val repository: CycleRepository,
    private val navigator: Navigator
): ViewModel() {

    val titleBuilder = UITitleBuilder()

    private val _uiTitle : StateFlow<UITitle> = titleBuilder.uiTitle.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UITitle()
    )

    private val _listMode = MutableStateFlow(ListMode.FILTERED)

    private val _screenFilter: MutableStateFlow<ScreenFilter> = MutableStateFlow(ScreenFilter.ALL)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _cycles: Flow<Result<List<Cycle>>> = combine(_listMode, _screenFilter) { m, f -> m to f }
        .flatMapLatest { (m, f) -> repository.query(m, f) }
        .map { content -> Result.Success(content) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading()
        )

    @Immutable
    data class UIState (
        val uiTitle: UITitle,
        val listMode : ListMode,
        val screenFilter : ScreenFilter,
        val cycles : List<Cycle>,
    )

    val uiState : StateFlow<Result<UIState>> = combine(_uiTitle, _listMode, _screenFilter, _cycles)
        { t, m, f, l ->
            when (l) {
                is Result.Success -> Result.Success(content = UIState(t, m, f,l.content))
                is Result.Error -> Result.Error(t)
                is Result.Loading -> Result.Loading(t)
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = Result.Loading()
        )

    sealed class UIEvent {
        data object OnBack: UIEvent()
        data class OnList(val newValue: ListMode): UIEvent()
        data class OnFilter(val newValue: ScreenFilter): UIEvent()
        data object OnCreate: UIEvent()
        data class OnEdit(val newValue: Cycle): UIEvent()
        data class OnDelete(val newValue: Cycle): UIEvent()
    }

    fun send (uiEvent : UIEvent) = viewModelScope.launch {
        when (uiEvent) {
            is UIEvent.OnBack -> navigator.navigateUp()
            is UIEvent.OnList -> _listMode.value = uiEvent.newValue
            is UIEvent.OnFilter -> _screenFilter.value = uiEvent.newValue
            is UIEvent.OnCreate -> navigator.navigate(
                destination = CyclesDestination.CreateScreen,
            )
            is UIEvent.OnEdit ->
                when(uiEvent.newValue.managementState) {
                    ManagementState.CREATED, ManagementState.BACKUP -> { }
                    ManagementState.HIDDEN ->
                        repository.show(uiEvent.newValue.cid)
                    ManagementState.VALID, ManagementState.INVALID ->
                        navigator.navigate(
                            destination = CyclesDestination.EditScreen(uiEvent.newValue.cid),
                        )
                }
            is UIEvent.OnDelete ->
                when(uiEvent.newValue.managementState) {
                    ManagementState.CREATED, ManagementState.BACKUP, ManagementState.HIDDEN ->
                        repository.delete(uiEvent.newValue)
                    ManagementState.VALID, ManagementState.INVALID ->
                        repository.hide(uiEvent.newValue.cid)
                }
        }
    }

}