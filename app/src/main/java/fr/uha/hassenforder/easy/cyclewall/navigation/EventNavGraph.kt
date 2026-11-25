package fr.uha.hassenforder.easy.cyclewall.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import fr.uha.hassenforder.easy.cyclewall.ui.event.ListEventScreen
import kotlinx.serialization.Serializable

sealed interface EventDestination : NavKey {
    @Serializable
    data object ListScreen : NavKey
}

fun EntryProviderScope<NavKey>.eventNavGraph () {
    entry<EventDestination.ListScreen> {
        ListEventScreen()
    }
}