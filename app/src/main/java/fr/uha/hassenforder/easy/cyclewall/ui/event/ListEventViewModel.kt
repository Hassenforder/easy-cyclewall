package fr.uha.hassenforder.easy.cyclewall.ui.event

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.easy.android.database.model.ListMode
import fr.uha.hassenforder.easy.android.flow.Result
import fr.uha.hassenforder.easy.android.navigation.Navigator
import fr.uha.hassenforder.easy.android.ui.app.UITitle
import fr.uha.hassenforder.easy.android.ui.app.UITitleBuilder
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao.ScreenFilter
import fr.uha.hassenforder.easy.cyclewall.dto.FullEvent
import fr.uha.hassenforder.easy.cyclewall.model.EventFactory
import fr.uha.hassenforder.easy.cyclewall.repository.EventRepository
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
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ListEventViewModel @Inject constructor (
    private val repository: EventRepository,
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
    private val _now = MutableStateFlow(LocalDateTime.now())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _events: Flow<Result<List<FullEvent>>> = _screenFilter
        .flatMapLatest { f -> repository.query(f) }
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
        val now : LocalDateTime,
        val screenFilter : ScreenFilter,
        val events : List<FullEvent>,
        val current : FullEvent?,
        val remainingTime : Long?,
    ) {
        companion object {
            fun create (uiTitle : UITitle, listMode : ListMode, now : LocalDateTime, screenFilter : ScreenFilter, events : List<FullEvent>) : UIState {
                val factory = EventFactory()
                val crt = factory.bestEvent(events, now)
                val remainingTime = factory.nextTimeInSeconds(events, now)
                return UIState(uiTitle, listMode, now, screenFilter, events, crt, remainingTime)
            }
        }
    }

    val uiState : StateFlow<Result<UIState>> = combine(_uiTitle, _listMode, _now, _screenFilter, _events)
        { t, m, n, s, l ->
            when (l) {
                is Result.Success -> Result.Success(content = UIState.create(t, m, n, s, l.content))
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
        data class OnNowChanged(val newValue: LocalDateTime): UIEvent()
        data class OnRefreshAll(val newValues: List<FullEvent>, val now : LocalDateTime): UIEvent()
        data class OnDelete(val newValue: FullEvent): UIEvent()
        data class OnRefresh(val newValue: FullEvent, val now : LocalDateTime): UIEvent()
    }

    fun send (uiEvent : UIEvent) = viewModelScope.launch {
        when (uiEvent) {
            is UIEvent.OnBack -> navigator.navigateUp()
            is UIEvent.OnList -> _listMode.value = uiEvent.newValue
            is UIEvent.OnFilter -> _screenFilter.value = uiEvent.newValue
            is UIEvent.OnNowChanged -> _now.value = uiEvent.newValue
            is UIEvent.OnRefreshAll -> repository.refreshAll(uiEvent.newValues, uiEvent.now)
            is UIEvent.OnDelete -> repository.delete(uiEvent.newValue)
            is UIEvent.OnRefresh -> repository.refresh(uiEvent.newValue, uiEvent.now)
        }
    }

}