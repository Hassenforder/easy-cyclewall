package fr.uha.hassenforder.easy.cyclewall.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import fr.uha.hassenforder.easy.cyclewall.ui.cycle.CreateCycleScreen
import fr.uha.hassenforder.easy.cyclewall.ui.cycle.EditCycleScreen
import fr.uha.hassenforder.easy.cyclewall.ui.cycle.ListCyclesScreen
import kotlinx.serialization.Serializable

sealed interface CyclesDestination : NavKey {
    @Serializable
    data object ListScreen: NavKey

    @Serializable
    data object CreateScreen: NavKey

    @Serializable
    data class EditScreen(val cid: Long): NavKey
}

fun EntryProviderScope<NavKey>.cyclesNavGraph () {
    entry<CyclesDestination.ListScreen> {
        ListCyclesScreen()
    }
    entry<CyclesDestination.CreateScreen> {
        CreateCycleScreen()
    }
    entry<CyclesDestination.EditScreen> {
        EditCycleScreen(cid = it.cid)
    }
}