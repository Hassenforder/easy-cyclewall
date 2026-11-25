package fr.uha.hassenforder.easy.cyclewall.ui.cycle

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.easy.android.flow.Result
import fr.uha.hassenforder.easy.android.navigation.Navigator
import fr.uha.hassenforder.easy.android.ui.app.UITitle
import fr.uha.hassenforder.easy.android.ui.app.UITitleBuilder
import fr.uha.hassenforder.easy.cyclewall.database.dao.CycleDao.CycleUpdate
import fr.uha.hassenforder.easy.cyclewall.model.Cycle
import fr.uha.hassenforder.easy.cyclewall.model.CycleRhythm
import fr.uha.hassenforder.easy.cyclewall.model.CycleScreen
import fr.uha.hassenforder.easy.cyclewall.repository.CycleRepository
import fr.uha.hassenforder.easy.cyclewall.validation.CycleValidationErrors
import fr.uha.hassenforder.easy.cyclewall.validation.CycleValidator
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class CycleViewModel @Inject constructor (
    private val repository: CycleRepository,
    private val navigator: Navigator
): ViewModel() {

    val uiTitleBuilder = UITitleBuilder()

    private val _uiTitle : StateFlow<UITitle> = uiTitleBuilder.uiTitle.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = UITitle()
    )

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    enum class Ending {
        NO_IDEA,
        BY_END,
        BY_DURATION,
    }

    private val _ending: MutableStateFlow<Ending> = MutableStateFlow(Ending.NO_IDEA)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _cycle: StateFlow<Result<Cycle>> = _id
        .flatMapLatest { id -> repository.getById(id) }
        .map { cycle ->
            if (cycle != null) {
                uiTitleBuilder.setDocumentName(cycle.title)
                _ending.value = when {
                    cycle.end == null && cycle.duration != null -> Ending.BY_DURATION
                    cycle.end != null && cycle.duration == null -> Ending.BY_END
                    else -> Ending.NO_IDEA
                }
                Result.Success(content = cycle)
            } else {
                Result.Error()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Result.Loading())

    @Immutable
    data class UIState(
        val uiTitle : UITitle,
        val cycle : Cycle,
        val ending : Ending,
        val validation : CycleValidationErrors,
        val isDayWithTime : Boolean,
    ) {
        companion object {
            fun create (uiTitle : UITitle, cycle : Cycle, ending : Ending, isDayWithTime : Boolean) : UIState {
                val validation = CycleValidator(cycle).validateAll()
                return UIState(uiTitle, cycle, ending, validation, isDayWithTime)
            }
        }

    }

    val uiState : StateFlow<Result<UIState>> = combine (_uiTitle, _cycle, _ending, repository.isDayWithTime)
        { t, c, e, idwt ->
            when (c) {
                is Result.Success -> Result.Success(content = UIState.create(t, c.content, e, idwt))
                is Result.Error -> Result.Error(t)
                is Result.Loading -> Result.Loading(t)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Result.Loading())

    sealed class UIEvent {
        data object OnBack: UIEvent()
        data class TitleChanged(val newValue: String): UIEvent()
        data class RhythmChanged(val newValue: CycleRhythm): UIEvent()
        data class ScreenChanged(val newValue: CycleScreen): UIEvent()
        data class BeginChanged(val newValue: LocalDateTime): UIEvent()
        data class EndingChanged(val newValue: Ending): UIEvent()
        data class EndChanged(val newValue: LocalDateTime): UIEvent()
        data class DurationChanged(val newValue: Int?): UIEvent()
        data class WallpaperChanged(val newValue: Uri?): UIEvent()
    }

    private fun validate (oldCycle : Cycle, uiEvent: UIEvent) : Boolean {
        val newCycle : Cycle = when (uiEvent) {
            is UIEvent.TitleChanged -> oldCycle.copy(title = uiEvent.newValue)
            is UIEvent.RhythmChanged -> oldCycle.copy(rhythm = uiEvent.newValue)
            is UIEvent.ScreenChanged -> oldCycle.copy(screen = uiEvent.newValue)
            is UIEvent.BeginChanged -> oldCycle.copy(begin = uiEvent.newValue)
            is UIEvent.EndChanged -> oldCycle.copy(end = uiEvent.newValue)
            is UIEvent.DurationChanged -> oldCycle.copy(duration = uiEvent.newValue)
            is UIEvent.WallpaperChanged -> oldCycle.copy(wallpaper = uiEvent.newValue)
            else -> return false
        }
        val validation = CycleValidator(newCycle).validateAll()
        val isValid = ! validation.hasError()
        return isValid
    }

    private suspend fun update (cycle : Cycle, uiEvent: UIEvent) {
        when (uiEvent) {
            is UIEvent.TitleChanged -> repository.update(CycleUpdate.Title(cid = cycle.cid, title = uiEvent.newValue))
            is UIEvent.RhythmChanged -> repository.update(CycleUpdate.Rhythm(cid = cycle.cid, rhythm = uiEvent.newValue))
            is UIEvent.ScreenChanged -> repository.update(CycleUpdate.Screen(cid = cycle.cid, screen = uiEvent.newValue))
            is UIEvent.BeginChanged -> repository.update(CycleUpdate.Begin(cid = cycle.cid, begin = uiEvent.newValue))
            is UIEvent.EndChanged -> repository.update(CycleUpdate.Ending(cid = cycle.cid, end = uiEvent.newValue, duration = null))
            is UIEvent.DurationChanged -> repository.update(CycleUpdate.Ending(cid = cycle.cid, end = null, duration = uiEvent.newValue))
            is UIEvent.WallpaperChanged -> repository.update(CycleUpdate.Wallpaper(cid = cycle.cid, wallpaper = uiEvent.newValue))
            else -> return
        }
    }

    fun send (uiEvent : UIEvent) = viewModelScope.launch {
        if (uiState.value !is Result.Success) return@launch
        val cycle : Cycle = (uiState.value as Result.Success<UIState>).content.cycle
        when (uiEvent) {
            is UIEvent.OnBack -> navigator.navigateUp()
            is UIEvent.EndingChanged -> _ending.value = uiEvent.newValue
            else -> {
                update(cycle, uiEvent)
                val isValid = validate(cycle, uiEvent)
                repository.valid(cycle.cid, isValid)
            }
        }
    }

    fun edit (pid : Long) = viewModelScope.launch {
        val pid : Long = repository.edit(pid)
        _id.value = pid
    }

    fun create(person: Cycle) = viewModelScope.launch {
        val pid : Long = repository.create(person)
        _id.value = pid
    }

}
